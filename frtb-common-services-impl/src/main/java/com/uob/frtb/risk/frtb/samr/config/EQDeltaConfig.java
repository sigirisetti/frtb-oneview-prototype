package com.uob.frtb.risk.frtb.samr.config;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import lombok.Getter;

import static com.uob.frtb.risk.frtb.samr.config.ConfigUtil.populateWithIntegerKeyAndDoubleValues;
import static com.uob.frtb.risk.frtb.samr.config.ConfigUtil.populateWithIntegers;

@Getter
@Component
public class EQDeltaConfig {

	@Value("${samr.eq.delta.buckets:1,2,3,4,5,6,7,8,9,10,11}")
	private String eqDeltaBucketConfig;

	@Value("${samr.eq.delta.repo.rw:0.0055,0.006,0.0045,0.0055,0.003,0.0035,0.004,0.005,0.007,0.005,0.007}")
	private String eqDeltaRepoRwConfig;

	@Value("${samr.eq.delta.non.repo.rw:0.55,0.6,0.45,0.55,0.3,0.35,0.4,0.5,0.7,0.5,0.7}")
	private String eqDeltaNonRepoRwConfig;

	@Value("${samr.eq.delta.spot.bucket.corr:0.15,0.15,0.15,0.15,0.25,0.25,0.25,0.25,0.075,0.125}")
	private String eqDeltaSpotBucketCorrConfig;

	@Value("${samr.eq.delta.repo.bucket.corr:0.15,0.15,0.15,0.15,0.25,0.25,0.25,0.25,0.075,0.125}")
	private String eqDeltaRepoBucketCorrConfig;

	@Value("${samr.eq.delta.spot.repo.bucket.corr:0.999,0.999,0.999,0.999,0.999,0.999,0.999,0.999,0.999,0.999}")
	private String eqDeltaSpotRepoBucketCorrConfig;

	@Value("${samr.eq.delta.sb.cross.corr:0.15}")
	private double eqDeltaSbCrossCorr;

	private List<Integer> eqDeltaBuckets = new ArrayList<>();
	private Map<Integer, Double> bucketRepoRwMap = new HashMap<>();
	private Map<Integer, Double> bucketNonRepoRwMap = new HashMap<>();
	private Map<Integer, Double> eqDeltaSpotBucketCorrMap = new HashMap<>();
	private Map<Integer, Double> eqDeltaRepoBucketCorrMap = new HashMap<>();
	private Map<Integer, Double> eqDeltaSpotRepoBucketCorrMap = new HashMap<>();

	@PostConstruct
	private void init() {
		populateWithIntegers(eqDeltaBucketConfig, eqDeltaBuckets);
		populateWithIntegerKeyAndDoubleValues(eqDeltaBuckets, eqDeltaRepoRwConfig, bucketRepoRwMap);
		populateWithIntegerKeyAndDoubleValues(eqDeltaBuckets, eqDeltaNonRepoRwConfig, bucketNonRepoRwMap);
		populateWithIntegerKeyAndDoubleValues(eqDeltaBuckets, eqDeltaSpotBucketCorrConfig, eqDeltaSpotBucketCorrMap);
		populateWithIntegerKeyAndDoubleValues(eqDeltaBuckets, eqDeltaRepoBucketCorrConfig, eqDeltaRepoBucketCorrMap);
		populateWithIntegerKeyAndDoubleValues(eqDeltaBuckets, eqDeltaSpotRepoBucketCorrConfig,
				eqDeltaSpotRepoBucketCorrMap);
	}

	public RealMatrix getEqSpotRepoBucketsCorrMatrix(int bucket, int size) {
		int dim = 2 * size;

		double crossRepoCorr = eqDeltaRepoBucketCorrMap.get(bucket);
		double crossSpotCorr = eqDeltaSpotBucketCorrMap.get(bucket);
		double crossSpotRepoDiagCorr = eqDeltaSpotRepoBucketCorrMap.get(bucket);
		double crossSpotRepoOffDiagCorr = crossSpotRepoDiagCorr * crossSpotCorr;

		RealMatrix m = new Array2DRowRealMatrix(dim, dim);

		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				if (i == j) {
					m.setEntry(i, j, 1);
				} else if (i < size && j < size) {
					m.setEntry(i, j, crossRepoCorr);
				} else if (i >= size && j >= size) {
					m.setEntry(i, j, crossSpotCorr);
				} else if (i >= size && j < size) {
					if (i - size == j) {
						m.setEntry(i, j, crossSpotRepoDiagCorr);
					} else {
						m.setEntry(i, j, crossSpotRepoOffDiagCorr);
					}
				} else if (i < size && j >= size) {
					if (j - size == i) {
						m.setEntry(i, j, crossSpotRepoDiagCorr);
					} else {
						m.setEntry(i, j, crossSpotRepoOffDiagCorr);
					}
				}
			}
		}
		return m;
	}
}
