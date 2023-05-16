package com.uob.frtb.risk.frtb.samr.config;

import com.uob.frtb.refdata.model.Tenor;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import lombok.Getter;

@Getter
@Component
public class CRNonSecVegaConfig {

	@Value("${samr.cr.non.sec.vega.rw:0.55}")
	private double vegaRwSigma;

	@Value("${samr.cr.non.sec.vega.lh:120}")
	private int lh;

	@Value("${samr.cr.non.sec.vega.alpha:0.01}")
	private double vegaOptAlpha;

	@Value("${samr.cr.non.sec.delta.tenor.buckets:0.5,1,3,5,10}")
	private String strCrNonSecDeltaTenorBuckets;

	@Value("${samr.cr.non.sec.delta.bucket1.tenor.corr:0.65}")
	private double crNonSecDeltaBucket1TenorCorr;

	@Value("${samr.cr.non.sec.delta.bucket1.inter.issuer.corr:0.35}")
	private double crNonSecDeltaBucket1InterIssuerCorr;

	@Value("${samr.cr.non.sec.delta.bucket1.basis.corr:0.999}")
	private double crNonSecDeltaBucket1BasisCorr;

	@Value("${samr.cr.non.sec.delta.issuer.buckets:1,2,3,4,5,6,7,8,9,10,11,12,13,14,15}")
	private String strCrNonSecDeltaIssuerBuckets;

	@Value("${samr.cr.non.sec.delta.bucket.rating.corr:0.5}")
	private double crNonSecDeltaBucketRatingCorr;

	@Value("${samr.cr.non.sec.delta.bucket1.bucket2.corr:0.75}")
	private double crNonSecDeltaB1B2Corr;

	@Value("${samr.cr.non.sec.delta.bucket1.bucket3.corr:0.1}")
	private double crNonSecDeltaB1B3Corr;

	@Value("${samr.cr.non.sec.delta.bucket1.bucket4.corr:0.2}")
	private double crNonSecDeltaB1B4Corr;

	@Value("${samr.cr.non.sec.delta.bucket1.bucket5.corr:0.25}")
	private double crNonSecDeltaB1B5Corr;

	@Value("${samr.cr.non.sec.delta.bucket1.bucket6.corr:0.2}")
	private double crNonSecDeltaB1B6Corr;

	@Value("${samr.cr.non.sec.delta.bucket1.bucket7.corr:0.15}")
	private double crNonSecDeltaB1B7Corr;

	@Value("${samr.cr.non.sec.delta.bucket1.bucket8.corr:0.1}")
	private double crNonSecDeltaB1B8Corr;

	@Value("${samr.cr.non.sec.delta.bucket2.bucket3.corr:0.05}")
	private double crNonSecDeltaB2B3Corr;

	@Value("${samr.cr.non.sec.delta.bucket2.bucket4.corr:0.15}")
	private double crNonSecDeltaB2B4Corr;

	@Value("${samr.cr.non.sec.delta.bucket2.bucket5.corr:0.2}")
	private double crNonSecDeltaB2B5Corr;

	@Value("${samr.cr.non.sec.delta.bucket2.bucket6.corr:0.15}")
	private double crNonSecDeltaB2B6Corr;

	@Value("${samr.cr.non.sec.delta.bucket2.bucket7.corr:0.1}")
	private double crNonSecDeltaB2B7Corr;

	@Value("${samr.cr.non.sec.delta.bucket3.bucket8.corr:0.1}")
	private double crNonSecDeltaB2B8Corr;

	@Value("${samr.cr.non.sec.delta.bucket3.bucket4.corr:0.05}")
	private double crNonSecDeltaB3B4Corr;

	@Value("${samr.cr.non.sec.delta.bucket3.bucket5.corr:0.15}")
	private double crNonSecDeltaB3B5Corr;

	@Value("${samr.cr.non.sec.delta.bucket3.bucket6.corr:0.20}")
	private double crNonSecDeltaB3B6Corr;

	@Value("${samr.cr.non.sec.delta.bucket3.bucket7.corr:0.05}")
	private double crNonSecDeltaB3B7Corr;

	@Value("${samr.cr.non.sec.delta.bucket3.bucket8.corr:0.2}")
	private double crNonSecDeltaB3B8Corr;

	@Value("${samr.cr.non.sec.delta.bucket4.bucket5.corr:0.2}")
	private double crNonSecDeltaB4B5Corr;

	@Value("${samr.cr.non.sec.delta.bucket4.bucket6.corr:0.25}")
	private double crNonSecDeltaB4B6Corr;

	@Value("${samr.cr.non.sec.delta.bucket4.bucket7.corr:0.05}")
	private double crNonSecDeltaB4B7Corr;

	@Value("${samr.cr.non.sec.delta.bucket4.bucket8.corr:0.05}")
	private double crNonSecDeltaB4B8Corr;

	@Value("${samr.cr.non.sec.delta.bucket5.bucket6.corr:0.25}")
	private double crNonSecDeltaB5B6Corr;

	@Value("${samr.cr.non.sec.delta.bucket5.bucket7.corr:0.05}")
	private double crNonSecDeltaB5B7Corr;

	@Value("${samr.cr.non.sec.delta.bucket5.bucket8.corr:0.15}")
	private double crNonSecDeltaB5B8Corr;

	@Value("${samr.cr.non.sec.delta.bucket6.bucket7.corr:0.05}")
	private double crNonSecDeltaB6B7Corr;

	@Value("${samr.cr.non.sec.delta.bucket6.bucket8.corr:0.2}")
	private double crNonSecDeltaB6B8Corr;

	@Value("${samr.cr.non.sec.delta.bucket7.bucket8.corr:0.05}")
	private double crNonSecDeltaB7B8Corr;

	private int[] tenorCodes;
	private List<Double> tenorBucketList;
	private List<Integer> issuerBucketList;

	private RealMatrix corr1;
	private RealMatrix corr2;
	private RealMatrix corr3;
	private RealMatrix corr4;
	private RealMatrix optCorrMat;

	private double[][] corr1Arr;
	private double[][] corr2Arr;
	private double[][] corr3Arr;
	private double[][] corr4Arr;
	private double[][] optCorrMatArr;

	private RealMatrix issuerBucketCorr;

	private double vegaRw;

	@PostConstruct
	private void init() {
		vegaRw = Math.min(vegaRwSigma * Math.sqrt(lh / 10), 1);
		tenorBucketList = new ArrayList<>();
		issuerBucketList = new ArrayList<>();
		ConfigUtil.populateWithIntegers(strCrNonSecDeltaIssuerBuckets, issuerBucketList);
		ConfigUtil.populateWithDouble(strCrNonSecDeltaTenorBuckets, tenorBucketList);
		tenorCodes = new int[tenorBucketList.size()];
		for (int i = 0; i < tenorCodes.length; i++) {
			tenorCodes[i] = (int) (tenorBucketList.get(i) * Tenor.DEFAULT_NUM_DAYS_IN_A_YEAR);
		}
		int dim = tenorBucketList.size();
		buildOptCorrMat(dim);
		buildCorr1(dim);
		buildCorr2();
		buildCorr3();
		buildCorr4();
		buildIssuerBucketCorr();
	}

	private void buildOptCorrMat(int dim) {
		optCorrMat = new Array2DRowRealMatrix(dim, dim);
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				if (i == j) {
					optCorrMat.setEntry(i, j, 1);
				} else {
					optCorrMat.setEntry(i, j,
							Math.exp(-vegaOptAlpha * Math.abs(tenorBucketList.get(i) - tenorBucketList.get(j))
									/ Math.min(tenorBucketList.get(i), tenorBucketList.get(j))));
				}
			}
		}
		optCorrMatArr = optCorrMat.getData();
	}

	private void buildIssuerBucketCorr() {
		issuerBucketCorr = new Array2DRowRealMatrix(issuerBucketList.size(), issuerBucketList.size());

		issuerBucketCorr.setEntry(0, 1, crNonSecDeltaB1B2Corr);
		issuerBucketCorr.setEntry(0, 2, crNonSecDeltaB1B3Corr);
		issuerBucketCorr.setEntry(0, 3, crNonSecDeltaB1B4Corr);
		issuerBucketCorr.setEntry(0, 4, crNonSecDeltaB1B5Corr);
		issuerBucketCorr.setEntry(0, 5, crNonSecDeltaB1B6Corr);
		issuerBucketCorr.setEntry(0, 6, crNonSecDeltaB1B7Corr);
		issuerBucketCorr.setEntry(0, 7, crNonSecDeltaB1B8Corr);

		issuerBucketCorr.setEntry(1, 2, crNonSecDeltaB2B3Corr);
		issuerBucketCorr.setEntry(1, 3, crNonSecDeltaB2B4Corr);
		issuerBucketCorr.setEntry(1, 4, crNonSecDeltaB2B5Corr);
		issuerBucketCorr.setEntry(1, 5, crNonSecDeltaB2B6Corr);
		issuerBucketCorr.setEntry(1, 6, crNonSecDeltaB2B7Corr);
		issuerBucketCorr.setEntry(1, 7, crNonSecDeltaB2B8Corr);

		issuerBucketCorr.setEntry(2, 3, crNonSecDeltaB3B4Corr);
		issuerBucketCorr.setEntry(2, 4, crNonSecDeltaB3B5Corr);
		issuerBucketCorr.setEntry(2, 5, crNonSecDeltaB3B6Corr);
		issuerBucketCorr.setEntry(2, 6, crNonSecDeltaB3B7Corr);
		issuerBucketCorr.setEntry(2, 7, crNonSecDeltaB3B8Corr);

		issuerBucketCorr.setEntry(3, 4, crNonSecDeltaB4B5Corr);
		issuerBucketCorr.setEntry(3, 5, crNonSecDeltaB4B6Corr);
		issuerBucketCorr.setEntry(3, 6, crNonSecDeltaB4B7Corr);
		issuerBucketCorr.setEntry(3, 7, crNonSecDeltaB4B8Corr);

		issuerBucketCorr.setEntry(4, 5, crNonSecDeltaB5B6Corr);
		issuerBucketCorr.setEntry(4, 6, crNonSecDeltaB5B7Corr);
		issuerBucketCorr.setEntry(4, 7, crNonSecDeltaB5B8Corr);

		issuerBucketCorr.setEntry(5, 6, crNonSecDeltaB6B7Corr);
		issuerBucketCorr.setEntry(5, 7, crNonSecDeltaB6B8Corr);

		issuerBucketCorr.setEntry(6, 7, crNonSecDeltaB7B8Corr);

		// Block 1 - top left
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (r == c) {
					issuerBucketCorr.setEntry(r, r, 1);
				} else if (r > c) {
					issuerBucketCorr.setEntry(r, c, issuerBucketCorr.getEntry(c, r));
				}
			}
		}
		// Block 2 - top right
		for (int r = 0; r < 8; r++) {
			for (int j = 8; j < issuerBucketList.size(); j++) {
				int c = j - 8;
				issuerBucketCorr.setEntry(r, j, crNonSecDeltaBucketRatingCorr * issuerBucketCorr.getEntry(r, c));
			}
		}

		// Block 3 - bottom left
		for (int i = 8; i < issuerBucketList.size(); i++) {
			for (int c = 0; c < 8; c++) {
				int r = i - 8;
				issuerBucketCorr.setEntry(i, c, crNonSecDeltaBucketRatingCorr * issuerBucketCorr.getEntry(r, c));
			}
		}

		// Block 4 - bottom left
		for (int i = 8; i < issuerBucketList.size(); i++) {
			for (int j = 8; j < issuerBucketList.size(); j++) {
				int r = i - 8;
				int c = j - 8;
				issuerBucketCorr.setEntry(i, j, issuerBucketCorr.getEntry(r, c));
			}
		}
	}

	private void buildCorr4() {
		corr4 = corr3.scalarMultiply(crNonSecDeltaBucket1BasisCorr);
		corr4Arr = corr4.getData();
	}

	private void buildCorr3() {
		corr3 = corr1.scalarMultiply(crNonSecDeltaBucket1InterIssuerCorr);
		corr3Arr = corr3.getData();
	}

	private void buildCorr2() {
		corr2 = corr1.scalarMultiply(crNonSecDeltaBucket1BasisCorr);
		corr2Arr = corr2.getData();
	}

	private void buildCorr1(int dim) {
		corr1 = new Array2DRowRealMatrix(dim, dim);
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				if (i == j) {
					corr1.setEntry(i, j, 1);
				} else {
					corr1.setEntry(i, j, crNonSecDeltaBucket1TenorCorr * optCorrMat.getEntry(i, j));
				}
			}
		}
		corr1Arr = corr1.getData();
	}
}