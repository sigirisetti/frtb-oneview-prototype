package com.uob.frtb.risk.frtb.samr.calculators;

import com.uob.frtb.risk.frtb.samr.config.EQDeltaConfig;
import com.uob.frtb.risk.frtb.samr.model.EquityInfo;
import com.uob.frtb.risk.frtb.samr.model.FrtbSensitivities;
import com.uob.frtb.risk.frtb.samr.model.RiskClass;
import com.uob.frtb.risk.frtb.samr.model.RiskClassAndSensitivity;
import com.uob.frtb.risk.frtb.samr.model.TradeSensitivity;
import com.uob.frtb.risk.frtb.samr.results.PoResults;
import com.uob.frtb.risk.frtb.samr.results.RiskClassHierarchyResultRow;

import lombok.*;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope("prototype")
public class EQDeltaRiskChargeCalc extends BaseRiskChargeCalculator {

	@Autowired
	private EQDeltaConfig eqDeltaConfig;

	private List<EquityDelta> deltas = new ArrayList<>();

	private Map<Integer, BucketWiseRiskCharge> riskCharges;

	private Map<GroupedEqKey, GroupedEqDelta> baseEqGrpDeltaMap;

	@Override
	protected void filter() {
		for (TradeSensitivity sens : calcRequest.getTradeSensitivities()) {
			if (RiskClassAndSensitivity.EQ_DELTA == sens.getSensitivityType()) {
				EquityDelta d = EquityDelta.fromTradeSensitivity(sens);
				deltas.add(d);
			}
		}
	}

	@Override
	protected void calculateRiskCharge() {
		if (deltas.isEmpty() || getCalcRequest().getEquityInfo() == null) {
			return;
		}
		if (!assignEquityInfo()) {
			messages.add("No Equity Issuer mapping info provided for Equity Delta");
			return;
		}
		Map<GroupedEqKey, EquityDelta> eqGrpDeltaMap = groupEquityDelta();
		applyRiskWeight(eqGrpDeltaMap);
		baseEqGrpDeltaMap = applyFx(eqGrpDeltaMap);
		debugGroupedData();
		riskCharges = calculateBucketWiseRiskCharge();
		for (Map.Entry<Integer, BucketWiseRiskCharge> me : riskCharges.entrySet()) {
			log.debug(me.getValue().toString());
		}

		List<Double> kb = riskCharges.entrySet().stream().map(e -> e.getValue().getKb()).collect(Collectors.toList());
		List<Double> sb = riskCharges.entrySet().stream().map(e -> e.getValue().getSb2()).collect(Collectors.toList());
		double lastBucketRiskCharge = computeRiskChargeForLastBucket();
		log.info("Last bucket risk charge : {}", lastBucketRiskCharge);

		riskCharge = calculateRiskCharge(eqDeltaConfig.getEqDeltaSbCrossCorr(), kb, sb) + lastBucketRiskCharge;
		log.info("EQ Delta risk charge : {}", riskCharge);
	}

	private double computeRiskChargeForLastBucket() {
		List<Integer> eqDeltaBuckets = eqDeltaConfig.getEqDeltaBuckets();
		int lastBucket = eqDeltaBuckets.get(eqDeltaBuckets.size() - 1);
		return baseEqGrpDeltaMap.entrySet().stream().filter(e -> (e.getKey().getBucket().intValue() == lastBucket))
				.map(e -> Math.abs(e.getValue().getDelta())).collect(Collectors.summingDouble(Double::doubleValue));
	}

	private Map<Integer, BucketWiseRiskCharge> calculateBucketWiseRiskCharge() {
		List<Integer> eqDeltaBuckets = eqDeltaConfig.getEqDeltaBuckets();
		Map<Integer, BucketWiseRiskCharge> riskCharges = new TreeMap<>();
		for (int i = 0; i < eqDeltaBuckets.size() - 1; i++) {
			Integer bucket = eqDeltaBuckets.get(i);
			log.debug("Handling bucket {} data", bucket);
			List<Double> repoDeltas = baseEqGrpDeltaMap.entrySet().stream()
					.filter(e -> (e.getKey().getBucket().equals(bucket.intValue()) && e.getKey().getRepo()))
					.map(e -> e.getValue().getDelta()).collect(Collectors.toList());
			List<Double> deltas = baseEqGrpDeltaMap.entrySet().stream()
					.filter(e -> (e.getKey().getBucket().equals(bucket.intValue()) && !e.getKey().getRepo()))
					.map(e -> e.getValue().getDelta()).collect(Collectors.toList());
			int dim = Math.max(repoDeltas.size(), deltas.size());
			if (dim == 0) {
				riskCharges.put(bucket, new BucketWiseRiskCharge(bucket, 0.0, 0.0, 0.0));
				continue;
			}
			RealMatrix vec = buildDeltaVector(repoDeltas, deltas, dim);
			RealMatrix mat = eqDeltaConfig.getEqSpotRepoBucketsCorrMatrix(bucket, dim);
			double kb = Math.sqrt(vec.transpose().multiply(mat).multiply(vec).getEntry(0, 0));
			double sb = repoDeltas.stream().collect(Collectors.summingDouble(Double::doubleValue));
			sb += deltas.stream().collect(Collectors.summingDouble(Double::doubleValue));
			double sb2 = Math.max(Math.min(kb, sb), -kb);
			riskCharges.put(bucket, new BucketWiseRiskCharge(bucket, kb, sb, sb2));
		}
		return riskCharges;
	}

	private RealMatrix buildDeltaVector(List<Double> repoDeltas, List<Double> deltas, int dim) {
		RealMatrix vec = new Array2DRowRealMatrix(2 * dim, 1);
		for (int i = 0; i < repoDeltas.size(); i++) {
			vec.setEntry(i, 0, repoDeltas.get(i));
		}
		for (int i = 0; i < deltas.size(); i++) {
			vec.setEntry(dim + i, 0, deltas.get(i));
		}
		return vec;
	}

	private void debugGroupedData() {
		for (Map.Entry<GroupedEqKey, GroupedEqDelta> me : baseEqGrpDeltaMap.entrySet()) {
			log.debug(me.getValue().toString());
		}
	}

	private Map<GroupedEqKey, GroupedEqDelta> applyFx(Map<GroupedEqKey, EquityDelta> eqGrpDeltaMap) {
		Map<GroupedEqKey, GroupedEqDelta> baseEqGrpDeltaMap = new TreeMap<>();
		for (Map.Entry<GroupedEqKey, EquityDelta> me : eqGrpDeltaMap.entrySet()) {
			GroupedEqKey key = (GroupedEqKey) me.getKey().clone();
			key.setCurrency(calcRequest.getBaseCurrency());
			GroupedEqDelta d = baseEqGrpDeltaMap.get(key);
			if (d == null) {
				d = new GroupedEqDelta(key);
				baseEqGrpDeltaMap.put(key, d);
			}
			if (!calcRequest.getBaseCurrency().equals(me.getKey().getCurrency())) {
				d.add(me.getValue().getDelta() * getRate(me.getValue().getCurrency()));
			} else {
				d.add(me.getValue().getDelta());
			}
		}
		return baseEqGrpDeltaMap;
	}

	private void applyRiskWeight(Map<GroupedEqKey, EquityDelta> eqGrpDeltaMap) {
		for (Map.Entry<GroupedEqKey, EquityDelta> me : eqGrpDeltaMap.entrySet()) {
			if (Boolean.TRUE.equals(me.getKey().getRepo())) {
				me.getValue().applyRiskWeight(eqDeltaConfig.getBucketRepoRwMap().get(me.getKey().getBucket()) * 10000);
			} else {
				me.getValue().applyRiskWeight(eqDeltaConfig.getBucketNonRepoRwMap().get(me.getKey().getBucket()) * 100);
			}
		}
	}

	private Map<GroupedEqKey, EquityDelta> groupEquityDelta() {
		Map<GroupedEqKey, EquityDelta> eqGrpDeltaMap = new TreeMap<>();
		for (EquityDelta d : deltas) {
			GroupedEqKey key = d.getEqGroupKey();
			EquityDelta existing = eqGrpDeltaMap.get(key);
			if (existing == null) {
				eqGrpDeltaMap.put(key, d);
			} else {
				existing.addDelta(d);
			}
		}
		return eqGrpDeltaMap;
	}

	private boolean assignEquityInfo() {
		Map<String, EquityInfo> tradeIdEqInfoMap = new HashMap<>();
		for (EquityInfo d : getCalcRequest().getEquityInfo()) {
			tradeIdEqInfoMap.put(d.getTradeIdentifier(), d);
		}
		boolean anyMappingDone = false;
		for (EquityDelta d : deltas) {
			EquityInfo ei = tradeIdEqInfoMap.get(d.getTradeIdentifier());
			if (ei == null) {
				messages.add(String.format("No Equity Issuer info for trade : %s ", d.getTradeIdentifier()));
				continue;
			}
			// TODO: Derive this
			d.setBucket(ei.getBucket());
			d.setIssuerId(ei.getIssuer());
			d.setIssuerName(ei.getIssuer());
			d.setRepo(ei.getRepo());
			anyMappingDone = true;
		}
		return anyMappingDone;
	}

	@Override
	void saveResults(PoResults results) {
		log.info("EQ Delta - Saving sensitivity results");
		if (deltas.isEmpty()) {
			return;
		}
		RiskClassHierarchyResultRow deltaResult = new RiskClassHierarchyResultRow(results,
				results.getSamrResults().getCurrency(), results.getSamrResults().getCurrency(),
				FrtbSensitivities.DELTA.toString(), getRiskClass().toString(), 0, riskCharge, 0, 0, 0, 0);
		results.addHierarchyResultRow(deltaResult);
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.EQ;
	}

	@Override
	String getSensitivity() {
		return FrtbSensitivities.DELTA.toString();
	}

	@Getter
	@Setter
	@ToString
	static class GroupedEqDelta {

		private GroupedEqKey key;
		private double delta;

		GroupedEqDelta(GroupedEqKey key) {
			this.key = key;
		}

		public void add(double d) {
			delta += d;
		}
	}

	@Getter
	@Setter
	static class EquityDelta {
		private String tradeIdentifier;
		private Integer bucket;
		private String issuerId;
		private String issuerName;
		private String currency;
		private Boolean repo;
		private double delta;

		EquityDelta(String tradeIdentifier, String currency, Boolean repo, double delta) {
			this.tradeIdentifier = tradeIdentifier;
			this.currency = currency;
			this.repo = repo;
			this.delta = delta;
		}

		public void applyRiskWeight(Double rw) {
			this.delta *= rw;
		}

		public void addDelta(EquityDelta d) {
			delta += d.getDelta();
		}

		public static EquityDelta fromTradeSensitivity(TradeSensitivity sens) {
			return new EquityDelta(sens.getTradeIdentifier(), sens.getCurrency(), sens.getRepo(),
					sens.getSensitivity());
		}

		public GroupedEqKey getEqGroupKey() {
			return new GroupedEqKey(bucket, issuerId, repo, currency);
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	static class GroupedEqKey implements Cloneable, Comparable<GroupedEqKey> {
		private Integer bucket;
		private String issuerId;
		private Boolean repo;
		private String currency;

		@Override
		public int compareTo(GroupedEqKey o) {
			return new CompareToBuilder().append(this.bucket, o.bucket).append(this.issuerId, o.issuerId)
					.append(this.repo, o.repo).append(this.currency, o.currency).toComparison();
		}

		@Override
		protected Object clone() {
			return new GroupedEqKey(bucket, issuerId, repo, currency);
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@ToString
	static class BucketWiseRiskCharge {
		private int bucket;
		private double kb;
		private double sb;
		private double sb2;
	}
}
