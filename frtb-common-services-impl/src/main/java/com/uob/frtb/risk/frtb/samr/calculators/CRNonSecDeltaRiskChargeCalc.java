package com.uob.frtb.risk.frtb.samr.calculators;

import com.uob.frtb.risk.frtb.samr.config.CRNonSecDeltaConfig;
import com.uob.frtb.risk.frtb.samr.model.FrtbSensitivities;
import com.uob.frtb.risk.frtb.samr.model.RiskClass;
import com.uob.frtb.risk.frtb.samr.model.RiskClassAndSensitivity;
import com.uob.frtb.risk.frtb.samr.results.PoResults;
import com.uob.frtb.risk.frtb.samr.results.RiskClassHierarchyResultRow;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.DoubleStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope("prototype")
public class CRNonSecDeltaRiskChargeCalc extends BaseCRNonSecRiskChargeCalc {

	@Autowired
	private CRNonSecDeltaConfig config;

	@Override
    protected void calculateRiskCharge() {
		if (sensitivities.isEmpty() || getCalcRequest().getCreditIssuerInfo().isEmpty()) {
			return;
		}
		if (!assignIssuerInfo()) {
			messages.add("No Credit Issuer mapping info provided for Credit Non Sec Delta");
			return;
		}
		Map<CurrGroupingKey, CurrGrouping> currencyDeltas = groupByCurrency();
		Map<GroupingKey, Group> bucketDeltas = applyFxRwAndSensFactor(currencyDeltas);
		Map<Integer, List<Group>> buckets = groupByBuckets(bucketDeltas);
		Map<Integer, BucketLevelResults> bucketLevelResults = calculateBucketLevelResults(buckets);
		calculateRiskCharge(bucketLevelResults);
	}

	private void calculateRiskCharge(Map<Integer, BucketLevelResults> bucketLevelResults) {
		List<Integer> issuerBuckets = config.getIssuerBucketList();
		RealMatrix vec = new Array2DRowRealMatrix(issuerBuckets.size(), 1);
		for (int i = 0; i < issuerBuckets.size(); i++) {
			if (bucketLevelResults.containsKey(i)) {
				vec.setEntry(i, 0, bucketLevelResults.get(i).getSb2());
			} else {
				vec.setEntry(i, 0, 0);
			}
		}
		RealMatrix corr = config.getIssuerBucketCorr();
		double d = vec.transpose().multiply(corr).multiply(vec).getEntry(0, 0);
		for (Map.Entry<Integer, BucketLevelResults> me : bucketLevelResults.entrySet()) {
			d = d - me.getValue().getSb2() * me.getValue().getSb2() + me.getValue().getKb() * me.getValue().getKb();
		}
		riskCharge = Math.sqrt(Math.max(d, 0));
	}

	private Map<Integer, BucketLevelResults> calculateBucketLevelResults(Map<Integer, List<Group>> buckets) {
		Map<Integer, BucketLevelResults> bucketLevelResults = new TreeMap<>();
		for (Map.Entry<Integer, List<Group>> me : buckets.entrySet()) {
			BucketLevelResults r = new BucketLevelResults();
			bucketLevelResults.put(me.getKey(), r);
			r.setBucket(me.getKey());
			List<Group> g = me.getValue();
			RealMatrix v = getDeltaVector(g);
			double sb = DoubleStream.of(v.getColumn(0)).sum();
			r.setSb(sb);
			RealMatrix c = buildIntraBucketCorrMatrix(g);
			double kb = Math.sqrt(Math.abs(v.transpose().multiply(c).multiply(v).getEntry(0, 0)));
			r.setKb(kb);
			r.setSb2(Math.max(Math.min(kb, sb), -kb));
			log.debug("CR Non Sec Delta bucket level result : {}", r.toString());
		}
		return bucketLevelResults;
	}

	private RealMatrix buildIntraBucketCorrMatrix(List<Group> groups) {
		List<Double> tenors = config.getTenorBucketList();
		int blockSize = tenors.size();
		int dim = groups.size() * blockSize;
		BlockRealMatrix bm = new BlockRealMatrix(dim, dim);
		double[][] corr1 = config.getCorr1Arr();
		for (int i = 0; i < dim; i += blockSize) {
			for (int j = 0; j < dim; j += blockSize) {
				if (i == j) {
					bm.setSubMatrix(corr1, i, j);
				} else {
					bm.setSubMatrix(getCorrMat(i / blockSize, j / blockSize, groups), i, j);
				}
			}
		}
		return bm;
	}

	private double[][] getCorrMat(int i, int j, List<Group> groups) {
		Group g1 = groups.get(i);
		Group g2 = groups.get(j);
		if (g1.getKey().getIssuerId().equals(g2.getKey().getIssuerId())) {
			if (g1.getKey().getBondOrCds().equals(g2.getKey().getBondOrCds())) {
				return config.getCorr1Arr();
			} else {
				return config.getCorr2Arr();
			}
		} else {
			if (g1.getKey().getBondOrCds().equals(g2.getKey().getBondOrCds())) {
				return config.getCorr3Arr();
			} else {
				return config.getCorr4Arr();
			}
		}
	}

	@Override
	protected int[] getTenorCodes() {
		return config.getTenorCodes();
	}

	@Override
	protected double getRw(int bucket) {
		return config.getRiskWeights().get(bucket - 1);
	}

	@Override
	void saveResults(PoResults results) {
		if (Double.compare(riskCharge, 0) == 0.0) {
			log.info("Credit Non Sec Delta - No results");
			return;
		}
		log.info("Credit Non Sec Delta - Saving results");
		RiskClassHierarchyResultRow deltaResult = new RiskClassHierarchyResultRow(results,
				results.getSamrResults().getCurrency(), results.getSamrResults().getCurrency(),
				FrtbSensitivities.DELTA.toString(), getRiskClass().toString(), 0, riskCharge, 0, 0, 0, 0);
		results.addHierarchyResultRow(deltaResult);
	}

	@Override
	String getSensitivity() {
		return FrtbSensitivities.DELTA.toString();
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.CREDIT_NS;
	}

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return RiskClassAndSensitivity.CR_NS_DELTA;
	}

	@Override
	protected double getSensFactor() {
		return 10000;
	}
}
