package com.uob.frtb.risk.frtb.samr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import lombok.Getter;

@Getter
@Component
public class EQCurvatureConfig {

	@Value("${samr.eq.curvature.buckets:1,2,3,4,5,6,7,8,9,10,11}")
	private String eqCurvatureBucketConfig;

	@Value("${samr.eq.curvature.rw:0.55,0.6,0.45,0.55,0.3,0.35,0.4,0.5,0.7,0.5,0.7}")
	private String eqCurvatureRw;

	@Value("${samr.eq.curvature.cvr.cross.corr:0.0225}")
	private String strEqCurvatureCvrCrossCorr;

	@Value("${samr.eq.curvature.sb.cross.corr:0.0225}")
	private String strEqCurvatureSbCrossCorr;

	private int lastBucket;
	private double eqCurvatureCvrCrossCorr;
	private double eqCurvatureSbCrossCorr;
	private List<Integer> eqCurvatureBuckets = new ArrayList<>();
	private Map<Integer, Double> riskWeights = new TreeMap<>();

	@PostConstruct
	private void init() {
		ConfigUtil.populateWithIntegers(eqCurvatureBucketConfig, eqCurvatureBuckets);
		ConfigUtil.populateWithIntegerKeyAndDoubleValues(eqCurvatureBuckets, eqCurvatureRw, riskWeights);
		lastBucket = eqCurvatureBuckets.get(eqCurvatureBuckets.size() - 1);
		eqCurvatureCvrCrossCorr = new Double(strEqCurvatureCvrCrossCorr);
		eqCurvatureSbCrossCorr = new Double(strEqCurvatureSbCrossCorr);
	}

	public double getEqCurvatureCvrCrossCorr() {
		return eqCurvatureCvrCrossCorr;
	}
}
