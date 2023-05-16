package com.uob.frtb.risk.frtb.samr.calculators;

import com.uob.frtb.core.exception.ApplicationException;
import com.uob.frtb.risk.frtb.samr.config.EQCurvatureConfig;
import com.uob.frtb.risk.frtb.samr.model.EquityInfo;
import com.uob.frtb.risk.frtb.samr.model.FrtbSensitivities;
import com.uob.frtb.risk.frtb.samr.model.RiskClass;
import com.uob.frtb.risk.frtb.samr.model.RiskClassAndSensitivity;
import com.uob.frtb.risk.frtb.samr.model.TradeSensitivity;
import com.uob.frtb.risk.frtb.samr.results.PoResults;
import com.uob.frtb.risk.frtb.samr.results.RiskClassHierarchyResultRow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope("prototype")
public class EQCurvatureRiskChargeCalc extends BaseRiskChargeCalculator {

	@Autowired
	private EQCurvatureConfig config;

	@Autowired
	private DebugUtils debugUtils;

	private List<EqCurvatureData> curvData = new ArrayList<>();

	private Map<Integer, BucketLevelSums> bucketLevelSums;

	@Override
	protected void filter() {
		for (TradeSensitivity sens : calcRequest.getTradeSensitivities()) {
			if (RiskClassAndSensitivity.EQ_CURVATURE == sens.getSensitivityType()) {
				EqCurvatureData d = EqCurvatureData.fromTradeSensitivity(sens);
				curvData.add(d);
			}
		}
	}

	@Override
	protected void calculateRiskCharge() throws ApplicationException {
		if (curvData.isEmpty()) {
			return;
		}
		assignEquityInfo();
		applyFx();
		applyRiskWeights();
		Map<String, EqCurvatureData> aggrByIssuer = aggregateByIssuer();
		calculateCvrData(aggrByIssuer);
		Map<Integer, List<BucketLevelCvrData>> bucketLevelDataMap = groupByIssuerAndBucket(aggrByIssuer);
		List<BucketLevelCvrData> lastBucketCvrData = bucketLevelDataMap.remove(config.getLastBucket());
		calculateCorrAndSimpleSums(bucketLevelDataMap);
		for (Map.Entry<Integer, BucketLevelSums> me : bucketLevelSums.entrySet()) {
			log.info("EQ Curvature 'bucket {}' data : {}", me.getKey(), me.getValue().toString());
		}
		debugUtils.writeToCsv(aggrByIssuer);
		double lastBucketCvrSum = 0;
		if (lastBucketCvrData != null) {
			for (BucketLevelCvrData dat : lastBucketCvrData) {
				lastBucketCvrSum += Math.abs(dat.getCvr());
			}
		}
		double kbSq = 0;
		double sbSq = 0;
		for (BucketLevelSums si : bucketLevelSums.values()) {
			kbSq = si.getKb() * si.getKb();
			for (BucketLevelSums sj : bucketLevelSums.values()) {
				if ((si.getSb2() > 0 || sj.getSb2() > 0) && si.getBucket() != sj.getBucket()) {
					sbSq += si.getSb2() * sj.getSb2();
				}
			}
		}
		riskCharge = Math.sqrt(kbSq + sbSq * config.getEqCurvatureSbCrossCorr()) + lastBucketCvrSum;

		log.info("EQ Curvature last bucket risk charge : {}", lastBucketCvrSum);
		log.info("EQ Curvature Risk charge : {}", riskCharge);
	}

	private void calculateCorrAndSimpleSums(Map<Integer, List<BucketLevelCvrData>> bucketLevelDataMap) {
		bucketLevelSums = new TreeMap<>();
		for (Map.Entry<Integer, List<BucketLevelCvrData>> me : bucketLevelDataMap.entrySet()) {
			List<BucketLevelCvrData> cvrData = me.getValue();
			double cvrSum = 0;
			double cvrSqSum = 0;
			double crossCvrSum = 0;
			for (BucketLevelCvrData dat : cvrData) {
				double cvr = dat.getCvr();
				if (cvr == 0) {
					continue;
				}
				cvrSum += cvr;
				double absCvr = Math.max(cvr, 0);
				cvrSqSum += absCvr * absCvr;
				double innerCvrSum = 0;
				for (BucketLevelCvrData innerDat : cvrData) {
					if ((cvr > 0 || innerDat.getCvr() > 0) && !dat.getIssuerId().equals(innerDat.getIssuerId())) {
						innerCvrSum += innerDat.getCvr();
					}
				}
				crossCvrSum = cvr * innerCvrSum;
			}
			BucketLevelSums bSum = new BucketLevelSums(me.getKey(),
					Math.sqrt(Math.max(0, cvrSqSum + crossCvrSum * config.getEqCurvatureCvrCrossCorr())), cvrSum);
			bucketLevelSums.put(me.getKey(), bSum);
		}
	}

	private Map<Integer, List<BucketLevelCvrData>> groupByIssuerAndBucket(Map<String, EqCurvatureData> aggrByIssuer) {
		Map<Integer, List<BucketLevelCvrData>> bucketLevelDataMap = new TreeMap<>();
		for (Map.Entry<String, EqCurvatureData> me : aggrByIssuer.entrySet()) {
			// BucketIssuerKey key = new
			// BucketIssuerKey(me.getValue().getBucket(),
			// me.getValue().getIssuerId());
			List<BucketLevelCvrData> list = bucketLevelDataMap.get(me.getValue().getBucket());
			EqCurvatureData eqCurvData = me.getValue();
			if (list == null) {
				list = new ArrayList<>();
				bucketLevelDataMap.put(me.getValue().getBucket(), list);
			}
			BucketLevelCvrData b = new BucketLevelCvrData();
			b.setBucket(eqCurvData.getBucket());
			b.setIssuerId(eqCurvData.getIssuerId());
			b.setCvr(eqCurvData.getCvr());
			b.setCvrUp(eqCurvData.getCvrUp());
			b.setCvrDown(eqCurvData.getCvrDown());
			list.add(b);
		}
		return bucketLevelDataMap;
	}

	private void calculateCvrData(Map<String, EqCurvatureData> aggrByIssuer) {
		for (Map.Entry<String, EqCurvatureData> me : aggrByIssuer.entrySet()) {
			me.getValue().calculateCvrData();
		}
	}

	private Map<String, EqCurvatureData> aggregateByIssuer() {
		Map<String, EqCurvatureData> aggrByIssuer = new HashMap<>();
		for (EqCurvatureData c : curvData) {
			EqCurvatureData eqData = aggrByIssuer.get(c.getIssuerId());
			if (eqData == null) {
				eqData = (EqCurvatureData) c.clone();
				aggrByIssuer.put(c.getIssuerId(), eqData);
			} else {
				eqData.add(c);
			}
		}
		return aggrByIssuer;
	}

	private void assignEquityInfo() throws ApplicationException {
		Map<String, EquityInfo> tradeIdEqInfoMap = new HashMap<>();
		for (EquityInfo d : getCalcRequest().getEquityInfo()) {
			tradeIdEqInfoMap.put(d.getTradeIdentifier(), d);
		}
		for (EqCurvatureData v : curvData) {
			EquityInfo ei = tradeIdEqInfoMap.get(v.getTradeIdentifier());
			if (ei == null) {
				throw new ApplicationException(-1, "No equity info for trade " + v.getTradeIdentifier());
			}
			v.setBucket(ei.getBucket());
			v.setIssuerId(ei.getIssuer());
		}
	}

	private void applyRiskWeights() {
		Map<Integer, Double> rws = config.getRiskWeights();
		for (EqCurvatureData c : curvData) {
			c.scaleDelta(rws.get(c.getBucket()) * 100);
		}
	}

	private void applyFx() {
		for (EqCurvatureData c : curvData) {
			c.scale(getRate(c.getCurrency()));
		}
	}

	@Override
	void saveResults(PoResults results) {
		log.info("EQ Curvature - Saving sensitivity results");
		if (curvData.isEmpty()) {
			return;
		}
		RiskClassHierarchyResultRow deltaResult = new RiskClassHierarchyResultRow(results,
				results.getSamrResults().getCurrency(), results.getSamrResults().getCurrency(),
				FrtbSensitivities.CURVATURE.toString(), getRiskClass().toString(), 0, riskCharge, 0, 0, 0, 0);
		results.addHierarchyResultRow(deltaResult);
	}

	@Getter
	@Setter
	@ToString
	private class BucketLevelSums {
		private int bucket;
		private double kb;
		private double sb;
		private double sb2;

		BucketLevelSums(int bucket, double kb, double sb) {
			this.bucket = bucket;
			this.kb = kb;
			this.sb = sb;
			sb2 = Math.max(Math.min(kb, sb), -kb);
		}
	}

	@Getter
	@Setter
	@EqualsAndHashCode
	private class BucketLevelCvrData {
		private int bucket;
		private String issuerId;
		private double cvrUp;
		private double cvr;
		private double cvrDown;
		private double kb;
		private double sb;
		private double sb2;
	}

	@Getter
	@Setter
	@EqualsAndHashCode
	@AllArgsConstructor
	private class BucketIssuerKey implements Comparable<BucketIssuerKey> {
		private int bucket;
		private String issuerId;

		@Override
		public int compareTo(BucketIssuerKey o) {
			if (bucket == o.getBucket()) {
				return issuerId.compareTo(o.getIssuerId());
			} else {
				return bucket < o.getBucket() ? -1 : 1;
			}
		}

	}

	@Getter
	@Setter
	@ToString
	static class EqCurvatureData implements Cloneable {
		private String tradeIdentifier;
		private String currency;
		private int bucket;
		private String issuerId;
		private double mtmUp;
		private double mtmBase;
		private double mtmDown;
		private double delta;
		private double cvrUp;
		private double cvrDown;
		private double cvr;

		EqCurvatureData(String tradeIdentifier, String currency, double mtmUp, double mtmBase, double mtmDown,
				double delta) {
			this.tradeIdentifier = tradeIdentifier;
			this.currency = currency;
			this.mtmUp = mtmUp;
			this.mtmBase = mtmBase;
			this.mtmDown = mtmDown;
			this.delta = delta;
		}

		EqCurvatureData(String tradeIdentifier, String currency, String issuerId, int bucket, double mtmUp,
				double mtmBase, double mtmDown, double delta) {
			this(tradeIdentifier, currency, mtmUp, mtmBase, mtmDown, delta);
			this.issuerId = issuerId;
			this.bucket = bucket;
		}

		public void calculateCvrData() {
			this.cvrUp = mtmUp - mtmBase - delta;
			this.cvrDown = mtmDown - mtmBase + delta;
			this.cvr = -Math.min(cvrUp, cvrDown);
		}

		public void scale(double rate) {
			this.mtmUp *= rate;
			this.mtmBase *= rate;
			this.mtmDown *= rate;
			this.delta *= rate;
		}

		public void scaleDelta(double d) {
			this.delta *= d;
		}

		void add(EqCurvatureData d) {
			this.mtmUp += d.mtmUp;
			this.mtmBase += d.mtmBase;
			this.mtmDown += d.mtmDown;
			this.delta += d.delta;
		}

		@Override
		public Object clone() {
			return new EqCurvatureData(tradeIdentifier, currency, issuerId, bucket, mtmUp, mtmBase, mtmDown, delta);
		}

		static EqCurvatureData fromTradeSensitivity(TradeSensitivity sens) {
			return new EqCurvatureData(sens.getTradeIdentifier(), sens.getCurrency(), sens.getMtmBpUpAmount(),
					sens.getMtmBaseAmount(), sens.getMtmBpDownAmount(), sens.getSensitivity());
		}
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.EQ;
	}

	@Override
	String getSensitivity() {
		return FrtbSensitivities.CURVATURE.toString();
	}
}
