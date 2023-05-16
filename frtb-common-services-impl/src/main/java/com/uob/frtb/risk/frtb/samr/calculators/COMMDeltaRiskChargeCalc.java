package com.uob.frtb.risk.frtb.samr.calculators;

import com.uob.frtb.risk.frtb.samr.config.COMMDeltaConfig;
import com.uob.frtb.risk.frtb.samr.model.CommodityInfo;
import com.uob.frtb.risk.frtb.samr.model.FrtbSensitivities;
import com.uob.frtb.risk.frtb.samr.model.RiskClass;
import com.uob.frtb.risk.frtb.samr.model.RiskClassAndSensitivity;
import com.uob.frtb.risk.frtb.samr.model.TradeSensitivity;
import com.uob.frtb.risk.frtb.samr.results.PoResults;
import com.uob.frtb.risk.frtb.samr.results.RiskClassHierarchyResultRow;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope("prototype")
public class COMMDeltaRiskChargeCalc extends BaseRiskChargeCalculator {

	@Autowired
	private COMMDeltaConfig config;

	private Map<CurrencyDeltaKey, DeltaMap> deltas;

	@Override
	protected void filter() {

		Map<String, CommodityInfo> m = calcRequest.getCommodityInfo().stream()
				.collect(Collectors.toMap(CommodityInfo::getTradeIdentifier, Function.identity()));

		deltas = new HashMap<>();
		for (TradeSensitivity sens : calcRequest.getTradeSensitivities()) {
			if (RiskClassAndSensitivity.COMM_DELTA == sens.getSensitivityType()) {
				if (!m.containsKey(sens.getTradeIdentifier())) {
					log.error("No commodity info provided for trade identifier : {}", sens.getTradeIdentifier());
					continue;
				}
				CommodityInfo info = m.get(sens.getTradeIdentifier());
				CurrencyDeltaKey key = new CurrencyDeltaKey(info.getBucket(), sens.getCurrency(), info.getUnderlying(),
						info.getGrade(), info.getDeliveryLocation());
				if (deltas.containsKey(key)) {
					deltas.get(key).add(sens);
				} else {
					deltas.put(key, DeltaMap.fromTradeSensitivity(sens));
				}
			}
		}
	}

	@Override
    protected void calculateRiskCharge() {
		Map<DeltaKey, DeltaMap> m = applyFxAndRw();

		Map<Integer, List<DeltaMap>> bucketedDeltas = new HashMap<>();
		Map<Integer, List<DeltaKey>> bucketedKeys = new HashMap<>();

		for (DeltaKey key : m.keySet()) {
			DeltaMap d = m.get(key);
			if (!bucketedDeltas.containsKey(key.getBucket())) {
				bucketedDeltas.put(key.getBucket(), new ArrayList<>());
				bucketedKeys.put(key.getBucket(), new ArrayList<>());
			}
			bucketedDeltas.get(key.getBucket()).add(d);
			bucketedKeys.get(key.getBucket()).add(key);
		}
		List<BucketWiseRiskCharge> bucketWiseRc = new ArrayList<>();
		for (int i = 0; i < config.getBuckets().size(); i++) {
			Integer b = config.getBuckets().get(i);
			if (bucketedDeltas.containsKey(b)) {
				RealMatrix deltaVector = buildDeltaVector(bucketedDeltas.get(b));
				RealMatrix corr = buildCorrelationMatrix(b, bucketedKeys.get(b));
				RealMatrix r = deltaVector.transpose().multiply(corr).multiply(deltaVector);
				double kb = Math.sqrt(r.getEntry(0, 0));
				double sb = Arrays.stream(deltaVector.getRow(0)).sum();
				double sb2 = Math.max(Math.min(kb, sb), -kb);
				bucketWiseRc.add(new BucketWiseRiskCharge(b, kb, sb, sb2));
			} else {
				bucketWiseRc.add(new BucketWiseRiskCharge(b, 0, 0, 0));
			}
		}

		BucketWiseRiskCharge last = bucketWiseRc.remove(bucketWiseRc.size() - 1);
		List<Double> kb = bucketWiseRc.stream().map(e -> e.getKb()).collect(Collectors.toList());
		List<Double> sb = bucketWiseRc.stream().map(e -> e.getSb2()).collect(Collectors.toList());

		double sum = calculateRiskCharge(config.getCommDeltaBucketCorr(), kb, sb);
		riskCharge = Math.sqrt(sum * sum + last.getSb2() * last.getSb2());
		log.info("Commodity Delta risk charge : {}", riskCharge);
	}

	private RealMatrix buildCorrelationMatrix(int bucket, List<DeltaKey> list) {
		int blockSize = config.getTenorBucketList().size();
		int dim = blockSize * list.size();
		BlockRealMatrix bm = new BlockRealMatrix(dim, dim);
		double[][] c1 = config.getCorr1();
		for (int i = 0; i < dim; i += blockSize) {
			for (int j = 0; j < dim; j += blockSize) {
				if (i == j) {
					bm.setSubMatrix(c1, i, j);
				} else {
					bm.setSubMatrix(deriveMat(i % blockSize, j % blockSize, bucket, list), i, j);
				}
			}
		}
		return bm;
	}

	private double[][] deriveMat(int i, int j, int bucket, List<DeltaKey> list) {
		DeltaKey key1 = list.get(i);
		DeltaKey key2 = list.get(j);
		if (key1.getUnderlying().equals(key2.getUnderlying())) {
			return config.getCorr2();
		} else {
			return getBucketCorr(bucket);
		}
	}

	private double[][] getBucketCorr(int bucket) {
		switch (bucket) {
		case 1:
			return config.getBucket1DiffCommodityWithBasis();
		case 2:
			return config.getBucket2DiffCommodityWithBasis();
		case 3:
			return config.getBucket3DiffCommodityWithBasis();
		case 4:
			return config.getBucket4DiffCommodityWithBasis();
		case 5:
			return config.getBucket5DiffCommodityWithBasis();
		case 6:
			return config.getBucket6DiffCommodityWithBasis();
		case 7:
			return config.getBucket7DiffCommodityWithBasis();
		case 8:
			return config.getBucket8DiffCommodityWithBasis();
		case 9:
			return config.getBucket9DiffCommodityWithBasis();
		case 10:
			return config.getBucket10DiffCommodityWithBasis();
		case 11:
			return config.getBucket11DiffCommodityWithBasis();
		}
		throw new IllegalArgumentException("Unknown bucket : " + bucket);
	}

	private RealMatrix buildDeltaVector(List<DeltaMap> list) {
		int blockSize = config.getTenorBucketList().size();
		double[] vec = new double[list.size() * blockSize];
		int i = 0;
		for (DeltaMap m : list) {
			for (Integer tnr : config.getMaturityTenorCodes()) {
				vec[i++] = m.getDeltaMap().containsKey(tnr) ? m.getDeltaMap().get(tnr) : 0;
			}
		}
		return new Array2DRowRealMatrix(vec);
	}

	private Map<DeltaKey, DeltaMap> applyFxAndRw() {
		Map<DeltaKey, DeltaMap> m = new HashMap<>();
		for (Map.Entry<CurrencyDeltaKey, DeltaMap> me : deltas.entrySet()) {
			CurrencyDeltaKey key = me.getKey();
			DeltaMap delta = me.getValue();
			double fac = config.getRiskWeights().get(key.getBucket() - 1) * getRate(key.getCurrency()) * 100;
			Map<Integer, Double> sensMap = new HashMap<>();
			for (Map.Entry<Integer, Double> m2 : delta.getDeltaMap().entrySet()) {
				sensMap.put(m2.getKey(), m2.getValue() * fac);
			}
			DeltaKey newKey = new DeltaKey(key.getBucket(), key.getUnderlying(), key.getGrade(), key.getLocation());
			if (!m.containsKey(newKey)) {
				m.put(newKey, new DeltaMap(sensMap));
			} else {
				m.get(newKey).add(sensMap);
			}
		}
		return m;
	}

	@Override
	void saveResults(PoResults results) {
		if (Double.compare(riskCharge, 0) == 0.0) {
			log.info("Commodity Delta - No results");
			return;
		}
		log.info("Commodity Delta - Saving results");
		RiskClassHierarchyResultRow deltaResult = new RiskClassHierarchyResultRow(results,
				results.getSamrResults().getCurrency(), results.getSamrResults().getCurrency(),
				FrtbSensitivities.DELTA.toString(), getRiskClass().toString(), 0, riskCharge, 0, 0, 0, 0);
		results.addHierarchyResultRow(deltaResult);
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	private static class DeltaKey implements Cloneable {
		private int bucket;
		private String underlying;
		private String grade;
		private String location;
	}

	@Getter
	@AllArgsConstructor
	@EqualsAndHashCode
	private static class CurrencyDeltaKey implements Cloneable {
		private int bucket;
		private String currency;
		private String underlying;
		private String grade;
		private String location;
	}

	@Setter
	@Getter
	@AllArgsConstructor
	private static class DeltaMap implements Cloneable {
		private Map<Integer, Double> deltaMap;

		public void add(Map<Integer, Double> sensMap) {
			for (Map.Entry<Integer, Double> me : sensMap.entrySet()) {
				if (deltaMap.containsKey(me.getKey())) {
					Double val = deltaMap.get(me.getKey());
					deltaMap.put(me.getKey(), me.getValue() + val);
				} else {
					deltaMap.put(me.getKey(), me.getValue());
				}
			}
		}

		public static DeltaMap fromTradeSensitivity(TradeSensitivity sens) {
			Map<Integer, Double> m = new HashMap<>();
			m.put(sens.getTenorCode(), sens.getSensitivity());
			return new DeltaMap(m);
		}

		public void add(TradeSensitivity sens) {
			if (!deltaMap.containsKey(sens.getTenorCode())) {
				deltaMap.put(sens.getTenorCode(), sens.getSensitivity());
			} else {
				double d = deltaMap.get(sens.getTenorCode());
				deltaMap.put(sens.getTenorCode(), sens.getSensitivity() + d);
			}
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

	@Override
	String getSensitivity() {
		return FrtbSensitivities.DELTA.toString();
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.COMMODITY;
	}
}
