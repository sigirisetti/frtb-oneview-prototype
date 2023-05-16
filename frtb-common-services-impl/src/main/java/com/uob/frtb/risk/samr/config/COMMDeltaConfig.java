package com.uob.frtb.risk.samr.config;

import com.uob.frtb.refdata.model.Tenor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import lombok.Getter;

import static com.uob.frtb.risk.samr.config.ConfigUtil.populateWithDouble;
import static com.uob.frtb.risk.samr.config.ConfigUtil.populateWithIntegers;

@Getter
@Component
public class COMMDeltaConfig {

	@Value("${samr.comm.delta.buckets:1,2,3,4,5,6,7,8,9,10,11}")
	private String strBuckets;

	@Value("${samr.comm.delta.tenor.buckets:0,0.25,0.5,1,2,3,5,10,15,20,30}")
	private String strCommDeltaTenorBuckets;

	@Value("${samr.comm.delta.buckets.rw:0.30,0.35,0.60,0.80,0.40,0.45,0.20,0.35,0.25,0.35,0.50}")
	private String strCommDeltaBucketRw;

	@Value("${samr.comm.delta.corr.tenor:0.99}")
	private double commDeltaCorrTenor;

	@Value("${samr.comm.delta.basis.corr.tenor:0.999}")
	private double commDeltaBasisCorrTenor;

	@Value("${samr.comm.delta.corr.bucket1:0.55}")
	private double commDeltaCorrBucket1;

	@Value("${samr.comm.delta.corr.bucket2:0.95}")
	private double commDeltaCorrBucket2;

	@Value("${samr.comm.delta.corr.bucket3:0.4}")
	private double commDeltaCorrBucket3;

	@Value("${samr.comm.delta.corr.bucket4:0.8}")
	private double commDeltaCorrBucket4;

	@Value("${samr.comm.delta.corr.bucket5:0.6}")
	private double commDeltaCorrBucket5;

	@Value("${samr.comm.delta.corr.bucket6:0.65}")
	private double commDeltaCorrBucket6;

	@Value("${samr.comm.delta.corr.bucket7:0.55}")
	private double commDeltaCorrBucket7;

	@Value("${samr.comm.delta.corr.bucket8:0.45}")
	private double commDeltaCorrBucket8;

	@Value("${samr.comm.delta.corr.bucket9:0.15}")
	private double commDeltaCorrBucket9;

	@Value("${samr.comm.delta.corr.bucket10:0.4}")
	private double commDeltaCorrBucket10;

	@Value("${samr.comm.delta.corr.bucket11:0.15}")
	private double commDeltaCorrBucket11;

	@Value("${samr.comm.delta.bucket.corr:0.2}")
	private double commDeltaBucketCorr;

	private List<Integer> buckets;
	private List<Double> tenorBucketList;
	private int[] maturityTenorCodes;
	private List<Double> riskWeights;
	private double[][] corr1;
	private double[][] corr2;
	private double[][] bucket1DiffCommodityWithBasis;
	private double[][] bucket2DiffCommodityWithBasis;
	private double[][] bucket3DiffCommodityWithBasis;
	private double[][] bucket4DiffCommodityWithBasis;
	private double[][] bucket5DiffCommodityWithBasis;
	private double[][] bucket6DiffCommodityWithBasis;
	private double[][] bucket7DiffCommodityWithBasis;
	private double[][] bucket8DiffCommodityWithBasis;
	private double[][] bucket9DiffCommodityWithBasis;
	private double[][] bucket10DiffCommodityWithBasis;
	private double[][] bucket11DiffCommodityWithBasis;

	@PostConstruct
	private void init() {
		buckets = new ArrayList<>();
		tenorBucketList = new ArrayList<>();
		riskWeights = new ArrayList<>();

		populateWithIntegers(strBuckets, buckets);
		populateWithDouble(strCommDeltaTenorBuckets, tenorBucketList);
		maturityTenorCodes = new int[tenorBucketList.size()];
		for (int i = 0; i < tenorBucketList.size(); i++) {
			maturityTenorCodes[i] = (int) (tenorBucketList.get(i) * Tenor.DEFAULT_NUM_DAYS_IN_A_YEAR);
		}
		populateWithDouble(strCommDeltaBucketRw, riskWeights);

		buildCorr1Matrix();
		buildCorr2Matrix();

		int dim = tenorBucketList.size();

		bucket1DiffCommodityWithBasis = new double[dim][dim];
		buildBucketCorrForDiffCommodityWithBasis(dim, commDeltaCorrBucket1, bucket1DiffCommodityWithBasis);

		bucket2DiffCommodityWithBasis = new double[dim][dim];
		buildBucketCorrForDiffCommodityWithBasis(dim, commDeltaCorrBucket2, bucket2DiffCommodityWithBasis);

		bucket3DiffCommodityWithBasis = new double[dim][dim];
		buildBucketCorrForDiffCommodityWithBasis(dim, commDeltaCorrBucket3, bucket3DiffCommodityWithBasis);

		bucket4DiffCommodityWithBasis = new double[dim][dim];
		buildBucketCorrForDiffCommodityWithBasis(dim, commDeltaCorrBucket4, bucket4DiffCommodityWithBasis);

		bucket5DiffCommodityWithBasis = new double[dim][dim];
		buildBucketCorrForDiffCommodityWithBasis(dim, commDeltaCorrBucket5, bucket5DiffCommodityWithBasis);

		bucket6DiffCommodityWithBasis = new double[dim][dim];
		buildBucketCorrForDiffCommodityWithBasis(dim, commDeltaCorrBucket6, bucket6DiffCommodityWithBasis);

		bucket7DiffCommodityWithBasis = new double[dim][dim];
		buildBucketCorrForDiffCommodityWithBasis(dim, commDeltaCorrBucket7, bucket7DiffCommodityWithBasis);

		bucket8DiffCommodityWithBasis = new double[dim][dim];
		buildBucketCorrForDiffCommodityWithBasis(dim, commDeltaCorrBucket8, bucket8DiffCommodityWithBasis);

		bucket9DiffCommodityWithBasis = new double[dim][dim];
		buildBucketCorrForDiffCommodityWithBasis(dim, commDeltaCorrBucket9, bucket9DiffCommodityWithBasis);

		bucket10DiffCommodityWithBasis = new double[dim][dim];
		buildBucketCorrForDiffCommodityWithBasis(dim, commDeltaCorrBucket10, bucket10DiffCommodityWithBasis);

		bucket11DiffCommodityWithBasis = new double[dim][dim];
		buildBucketCorrForDiffCommodityWithBasis(dim, commDeltaCorrBucket11, bucket11DiffCommodityWithBasis);
	}

	private void buildBucketCorrForDiffCommodityWithBasis(int size, double corr, double[][] m) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				m[i][j] = corr2[i][j] * corr;
			}
		}
	}

	private void buildCorr2Matrix() {
		int dim = tenorBucketList.size();
		corr2 = new double[dim][dim];
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				corr2[i][j] = corr1[i][j] * commDeltaBasisCorrTenor;
			}
		}
	}

	private void buildCorr1Matrix() {
		int dim = tenorBucketList.size();
		corr1 = new double[dim][dim];
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				corr1[i][j] = (i == j ? 1 : commDeltaCorrTenor);
			}
		}
	}
}
