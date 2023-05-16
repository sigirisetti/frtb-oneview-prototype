package com.uob.frtb.risk.frtb.samr.calculators;

import com.uob.frtb.core.exception.ApplicationException;
import com.uob.frtb.risk.frtb.samr.config.EQVegaConfig;
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
import java.util.stream.DoubleStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope("prototype")
public class EQVegaRiskChargeCalc extends BaseRiskChargeCalculator {

	@Autowired
	private EQVegaConfig eqVegaConfig;

	private List<EquityVega> vegas = new ArrayList<>();

	private Map<Integer, BucketWiseRiskCharge> bucketLevelRcMap;

	@Override
	protected void filter() {
		for (TradeSensitivity sens : calcRequest.getTradeSensitivities()) {
			if (RiskClassAndSensitivity.EQ_VEGA == sens.getSensitivityType()) {
				EquityVega d = EquityVega.fromTradeSensitivity(sens);
				vegas.add(d);
			}
		}
	}

	@Override
	protected void calculateRiskCharge() throws ApplicationException {
		if (vegas.isEmpty() || getCalcRequest().getEquityInfo() == null) {
			return;
		}
		assignEquityInfo();
		Map<GroupedEqKey, EquityVega> eqGrpVegaMap = groupEquityVega();
		applyRwAndScale(eqGrpVegaMap);
		applyFx(eqGrpVegaMap);
		buildBucketLevelRiskCharge(eqGrpVegaMap);
		double lastBucketRiskCharge = computeRiskChargeForLastBucket(eqGrpVegaMap);
		calculateRiskCharge(eqVegaConfig.getEqVegaSbCrossCorr(), bucketLevelRcMap, lastBucketRiskCharge);
		log.info("EQ Delta risk charge : {}", riskCharge);
	}

	private void calculateRiskCharge(double sbCrossCorr, Map<Integer, BucketWiseRiskCharge> bucketLevelKbSbMap,
			double lastBucketRiskCharge) {
		List<Double> kb = bucketLevelKbSbMap.entrySet().stream().map(e -> e.getValue().getKb())
				.collect(Collectors.toList());
		List<Double> sb = bucketLevelKbSbMap.entrySet().stream().map(e -> e.getValue().getSb2())
				.collect(Collectors.toList());
		riskCharge = calculateRiskCharge(sbCrossCorr, kb, sb) + lastBucketRiskCharge;
	}

	private double computeRiskChargeForLastBucket(Map<GroupedEqKey, EquityVega> eqGrpVegaMap) {
		int n = eqVegaConfig.getEqVegaBuckets().size();
		int lastBucket = eqVegaConfig.getEqVegaBuckets().get(n - 1);
		return eqGrpVegaMap.entrySet().stream().filter(e -> (e.getKey().getBucket() == lastBucket))
				.map(e -> Math.abs(e.getValue().getVega())).collect(Collectors.summingDouble(Double::doubleValue));
	}

	private void buildBucketLevelRiskCharge(Map<GroupedEqKey, EquityVega> eqGrpDeltaMap) {
		Map<Integer, List<EquityVega>> issuersGroupedByBucket = eqGrpDeltaMap.values().stream()
				.collect(Collectors.groupingBy(EquityVega::getBucket));
		bucketLevelRcMap = new TreeMap<>();
		int n = eqVegaConfig.getEqVegaBuckets().size();
		int lastBucket = eqVegaConfig.getEqVegaBuckets().get(n - 1);
		for (Map.Entry<Integer, List<EquityVega>> me : issuersGroupedByBucket.entrySet()) {
			if (me.getKey().intValue() == lastBucket) {
				continue;
			}
			log.info("EQ Vega - Calculating bucket level risk charge : {}", me.getKey().toString());
			Map<String, Map<Integer, EquityVega>> issuersEquityVegaMap = buildIssuerEquityVegaMap(me);
			int nIssuers = issuersEquityVegaMap.size();
			RealMatrix corr = eqVegaConfig.buildCorrMatForRiskChargeCalc(nIssuers, me.getKey());
			RealMatrix vec = getVegaVector(issuersEquityVegaMap);
			double kb = Math.sqrt(Math.max(vec.transpose().multiply(corr).multiply(vec).getEntry(0, 0), 0));
			double sb = DoubleStream.of(vec.getColumn(0)).sum();
			double sb2 = Math.max(Math.min(kb, sb), -kb);
			bucketLevelRcMap.put(me.getKey(), new BucketWiseRiskCharge(me.getKey(), kb, sb, sb2));
			log.info("EQ Vega - Bucket {} risk charge : {}", me.getKey(), kb);
		}
	}

	private Map<String, Map<Integer, EquityVega>> buildIssuerEquityVegaMap(Map.Entry<Integer, List<EquityVega>> me) {
		Map<String, Map<Integer, EquityVega>> issuersEquityVegaMap = new HashMap<>();
		for (EquityVega ev : me.getValue()) {
			Map<Integer, EquityVega> m = issuersEquityVegaMap.get(ev.getIssuerId());
			if (m == null) {
				m = new HashMap<>();
				issuersEquityVegaMap.put(ev.getIssuerId(), m);
			}
			m.put(ev.getTenorCode(), ev);
		}
		return issuersEquityVegaMap;
	}

	private RealMatrix getVegaVector(Map<String, Map<Integer, EquityVega>> issuersEquityVegaMap) {
		int[] tenorCodes = eqVegaConfig.getOptionMaturityTenorCodes();
		RealMatrix vec = new Array2DRowRealMatrix(issuersEquityVegaMap.size() * tenorCodes.length, 1);
		int k = 0;
		for (Map<Integer, EquityVega> m : issuersEquityVegaMap.values()) {
			for (int j = 0; j < tenorCodes.length; j++) {
				if (m.containsKey(tenorCodes[j])) {
					vec.setEntry(k++, 0, m.get(tenorCodes[j]).getVega());
				}
			}
		}
		return vec;
	}

	private void applyFx(Map<GroupedEqKey, EquityVega> eqGrpDeltaMap) {
		for (Map.Entry<GroupedEqKey, EquityVega> me : eqGrpDeltaMap.entrySet()) {
			me.getValue().scale(getRate(me.getValue().getCurrency()));
		}
	}

	private void applyRwAndScale(Map<GroupedEqKey, EquityVega> eqGrpDeltaMap) {
		for (Map.Entry<GroupedEqKey, EquityVega> me : eqGrpDeltaMap.entrySet()) {
			me.getValue().scale(eqVegaConfig.getEqVegaLargeCapRw() * 100);
		}
	}

	private Map<GroupedEqKey, EquityVega> groupEquityVega() {
		Map<GroupedEqKey, EquityVega> eqGrpDeltaMap = new TreeMap<>();
		for (EquityVega d : vegas) {
			GroupedEqKey key = d.getEqGroupKey();
			EquityVega existing = eqGrpDeltaMap.get(key);
			if (existing == null) {
				eqGrpDeltaMap.put(key, d);
			} else {
				existing.addVega(d);
			}
		}
		return eqGrpDeltaMap;
	}

	private void assignEquityInfo() throws ApplicationException {
		Map<String, EquityInfo> tradeIdEqInfoMap = new HashMap<>();
		for (EquityInfo d : getCalcRequest().getEquityInfo()) {
			tradeIdEqInfoMap.put(d.getTradeIdentifier(), d);
		}
		for (EquityVega v : vegas) {
			EquityInfo ei = tradeIdEqInfoMap.get(v.getTradeIdentifier());
			if (ei == null) {
				throw new ApplicationException(-1, "No equity info for trade " + v.getTradeIdentifier());
			}
			v.setBucket(ei.getBucket());
			v.setIssuerId(ei.getIssuer());
			v.setIssuerName(ei.getIssuer());
		}
	}

	@Override
	void saveResults(PoResults results) {
		log.info("EQ Vega - Saving sensitivity results");
		if (bucketLevelRcMap == null) {
			return;
		}
		RiskClassHierarchyResultRow deltaResult = new RiskClassHierarchyResultRow(results,
				results.getSamrResults().getCurrency(), results.getSamrResults().getCurrency(),
				FrtbSensitivities.VEGA.toString(), getRiskClass().toString(), 0, riskCharge, 0, 0, 0, 0);
		results.addHierarchyResultRow(deltaResult);
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.EQ;
	}

	@Override
	String getSensitivity() {
		return FrtbSensitivities.VEGA.toString();
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

	@Getter
	@Setter
	@AllArgsConstructor
	@EqualsAndHashCode
	@ToString
	static class GroupedEqKey implements Cloneable, Comparable<GroupedEqKey> {
		private Integer bucket;
		private String issuerId;
		private String currency;
		private int tenorCode;

		@Override
		public int compareTo(GroupedEqKey o) {
			return new CompareToBuilder().append(this.bucket, o.bucket).append(this.issuerId, o.issuerId)
					.append(this.currency, o.currency).append(this.tenorCode, o.tenorCode).toComparison();
		}

		@Override
        protected Object clone() {
			return new GroupedEqKey(bucket, issuerId, currency, tenorCode);
		}
	}

	@Getter
	@Setter
	static class EquityVega {
		private String tradeIdentifier;
		private Integer bucket;
		private String issuerId;
		private String issuerName;
		private String currency;
		private int tenorCode;
		private double vega;

		EquityVega(String tradeIdentifier, String currency, int tenorCode, double vega) {
			this.tradeIdentifier = tradeIdentifier;
			this.currency = currency;
			this.tenorCode = tenorCode;
			this.vega = vega;
		}

		public void scale(Double factor) {
			this.vega *= factor;
		}

		public void addVega(EquityVega d) {
			vega += d.getVega();
		}

		public static EquityVega fromTradeSensitivity(TradeSensitivity sens) {
			return new EquityVega(sens.getTradeIdentifier(), sens.getCurrency(), sens.getTenorCode(),
					sens.getSensitivity());
		}

		public GroupedEqKey getEqGroupKey() {
			return new GroupedEqKey(bucket, issuerId, currency, tenorCode);
		}
	}
}
