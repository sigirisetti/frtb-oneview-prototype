package com.uob.frtb.risk.samr.config;

import com.uob.frtb.refdata.model.Tenor;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import lombok.Getter;

@Getter
@Component
public class EQVegaConfig {

	@Value("${samr.eq.vega.buckets:1,2,3,4,5,6,7,8,9,10,11}")
	private String eqVegaBucketConfig;

	@Value("${samr.eq.vega.opt.mat.alpha:0.01}")
	private double eqVegaOptMatAlpha;

	@Value("${samr.eq.delta.bucket1.spot.corr:0.15}")
	private double eqDeltaBucket1SpotCorr;

	@Value("${samr.eq.delta.bucket5.spot.corr:0.25}")
	private double eqDeltaBucket5SpotCorr;

	@Value("${samr.eq.delta.bucket9.spot.corr:0.075}")
	private double eqDeltaBucket9SpotCorr;

	@Value("${samr.eq.delta.bucket10.spot.corr:0.0125}")
	private double eqDeltaBucket10SpotCorr;

	@Value("${samr.eq.vega.rw:0.55}")
	private double eqVegaRw;

	@Value("${samr.eq.vega.smallCap.lh:60}")
	private double eqVegaSmallCapLh;

	@Value("${samr.eq.vega.largeCap.lh:20}")
	private double eqVegaLargeCapLh;

	@Value("${samr.eq.vega.opt.mat.tenors:0.5,1,3,5,10}")
	private String eqVegaOptMatTenorConfig;

	@Value("${samr.eq.vega.sb.cross.corr:0.15}")
	private double eqVegaSbCrossCorr;

	private List<Integer> eqVegaBuckets = new ArrayList<>();
	private List<Double> eqVegaOptMatTenors = new ArrayList<>();
	private int[] optionMaturityTenorCodes;
	private RealMatrix eqVegaCorrWithInSameBucketAndIssuer;
	private RealMatrix eqVegaCorrWithInBucket1To4AndDiffIssuer;
	private RealMatrix eqVegaCorrWithInBucket5To8AndDiffIssuer;
	private RealMatrix eqVegaCorrWithInBucket9AndDiffIssuer;
	private RealMatrix eqVegaCorrWithInBucket10AndDiffIssuer;
	private double eqVegaSmallCapRw;
	private double eqVegaLargeCapRw;

	@PostConstruct
	private void init() {
		ConfigUtil.populateWithIntegers(eqVegaBucketConfig, eqVegaBuckets);
		ConfigUtil.populateWithDouble(eqVegaOptMatTenorConfig, eqVegaOptMatTenors);
		optionMaturityTenorCodes = new int[eqVegaOptMatTenors.size()];
		for (int i = 0; i < eqVegaOptMatTenors.size(); i++) {
			optionMaturityTenorCodes[i] = (int) (eqVegaOptMatTenors.get(i) * Tenor.DEFAULT_NUM_DAYS_IN_A_YEAR);
		}
		eqVegaCorrWithInSameBucketAndIssuer = buildOptionMaturityCorrelation(1);
		eqVegaCorrWithInBucket1To4AndDiffIssuer = buildOptionMaturityCorrelation(eqDeltaBucket1SpotCorr);
		eqVegaCorrWithInBucket5To8AndDiffIssuer = buildOptionMaturityCorrelation(eqDeltaBucket5SpotCorr);
		eqVegaCorrWithInBucket9AndDiffIssuer = buildOptionMaturityCorrelation(eqDeltaBucket9SpotCorr);
		eqVegaCorrWithInBucket10AndDiffIssuer = buildOptionMaturityCorrelation(eqDeltaBucket10SpotCorr);
		eqVegaSmallCapRw = Math.min(eqVegaRw * Math.sqrt(eqVegaSmallCapLh * 0.1), 1);
		eqVegaLargeCapRw = Math.min(eqVegaRw * Math.sqrt(eqVegaLargeCapLh * 0.1), 1);
	}

	private RealMatrix buildOptionMaturityCorrelation(double scalingFactor) {
		int dim = eqVegaOptMatTenors.size();
		RealMatrix optMatCorr = new Array2DRowRealMatrix(dim, dim);
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				double val = Math
						.exp(-eqVegaOptMatAlpha * Math.abs(eqVegaOptMatTenors.get(i) - eqVegaOptMatTenors.get(j))
								/ Math.min(eqVegaOptMatTenors.get(i), eqVegaOptMatTenors.get(j)));
				optMatCorr.setEntry(i, j, val * scalingFactor);
			}
		}
		return optMatCorr;
	}

	public RealMatrix buildCorrMatForRiskChargeCalc(int nIssuers, int bucket) {
		int blockSize = eqVegaOptMatTenors.size();
		int dim = blockSize * nIssuers;
		BlockRealMatrix bm = new BlockRealMatrix(dim, dim);
		double[][] m1 = eqVegaCorrWithInSameBucketAndIssuer.getData();
		double[][] m2 = getMatrixByBucket(bucket).getData();
		for (int i = 0; i < dim; i += blockSize) {
			for (int j = 0; j < dim; j += blockSize) {
				if (i == j) {
					bm.setSubMatrix(m1, i, j);
				} else {
					bm.setSubMatrix(m2, i, j);
				}
			}
		}
		return bm;
	}

	private RealMatrix getMatrixByBucket(int bucket) {
		if (bucket < 5) {
			return eqVegaCorrWithInBucket1To4AndDiffIssuer;
		} else if (bucket < 9) {
			return eqVegaCorrWithInBucket5To8AndDiffIssuer;
		} else if (bucket == 9) {
			return eqVegaCorrWithInBucket9AndDiffIssuer;
		} else if (bucket == 10) {
			return eqVegaCorrWithInBucket10AndDiffIssuer;
		}
		throw new IllegalStateException("Bucket " + bucket + " is not supported");
	}
}
