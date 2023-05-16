package com.uob.frtb.risk.frtb.samr.config;

import com.uob.frtb.risk.frtb.samr.model.RiskWeight;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

/**
 * IR delta configuration helper
 * 
 * @author Surya
 */
@Component
public class IRDeltaConfig {

	@Value("${samr.ir.delta.corr.theta:0.03}")
	private double irCorrTheta;

	@Value("${samr.ir.delta.corr.theta.multiplier:0.999}")
	private double interRateCorrThetaMultiplier;

	@Value("${samr.ir.delta.cross.bucket.corr:0.5}")
	private double irCrossBucketCorr;

	private RealMatrix defaultRatesCorr;

	private List<RiskWeight> defaultRiskWeights = new ArrayList<>();

	@PostConstruct
	private void init() {
		defaultRiskWeights = new ArrayList<>();
		defaultRiskWeights.add(new RiskWeight(1, 0.25, 0.024));
		defaultRiskWeights.add(new RiskWeight(1, 0.5, 0.024));
		defaultRiskWeights.add(new RiskWeight(1, 1, 0.0225));
		defaultRiskWeights.add(new RiskWeight(1, 2, 0.0188));
		defaultRiskWeights.add(new RiskWeight(1, 3, 0.0173));
		defaultRiskWeights.add(new RiskWeight(1, 5, 0.015));
		defaultRiskWeights.add(new RiskWeight(1, 7, 0.015));
		defaultRiskWeights.add(new RiskWeight(1, 10, 0.015));
		defaultRiskWeights.add(new RiskWeight(1, 15, 0.015));
		defaultRiskWeights.add(new RiskWeight(1, 30, 0.015));

		defaultRatesCorr = buildDefaultRatesCorrelation();
	}

	public double getIrCrossBucketCorr() {
		return irCrossBucketCorr;
	}

	public List<RiskWeight> getDefaultRiskWeights() {
		return defaultRiskWeights;
	}

	/**
	 * Constructs rates correlation among different rate indexes.
	 * 
	 * Matrix is constructed from block matrices, each representing one rate
	 * index.
	 * 
	 * Diagonal block matrices for a same rate index and off diagonal blocks for
	 * different indexes
	 * 
	 * @param nIndexes
	 * @return
	 */
	public RealMatrix ratesCorrelation(int nIndexes) {
		int blockSize = defaultRiskWeights.size();
		int dim = blockSize * nIndexes;
		RealMatrix m = new Array2DRowRealMatrix(dim, dim);
		double[][] diagMat = ratesCorrelation().getData();
		double[][] offDiagMat = interRatesCorrelation().getData();
		for (int i = 0; i < nIndexes; i++) {
			for (int j = 0; j < nIndexes; j++) {
				if (i == j) {
					m.setSubMatrix(diagMat, i * blockSize, i * blockSize);
				} else {
					m.setSubMatrix(offDiagMat, i * blockSize, j * blockSize);
				}
			}
		}
		return m;
	}

	private RealMatrix buildDefaultRatesCorrelation() {
		return ratesCorrelation(defaultRiskWeights);
	}

	/**
	 * Default risk weights by FRTB
	 * 
	 * @return
	 */
	public RealMatrix ratesCorrelation() {
		return new Array2DRowRealMatrix(defaultRatesCorr.getData());
	}

	/**
	 * Rates correlation between different tenors of same index
	 * 
	 * @param riskWeights
	 * @return
	 */
	private RealMatrix ratesCorrelation(List<RiskWeight> riskWeights) {
		RealMatrix corr = new Array2DRowRealMatrix(riskWeights.size(), riskWeights.size());
		for (int i = 0; i < riskWeights.size(); i++) {
			for (int j = 0; j < riskWeights.size(); j++) {
				if (i == j) {
					corr.setEntry(i, i, 1);
				} else if (i < j) {
					corr.setEntry(i, j, calculateRatesCorrelation(riskWeights.get(i), riskWeights.get(j)));
				} else {
					corr.setEntry(i, j, corr.getEntry(j, i));
				}
			}
		}
		return corr;
	}

	/**
	 * 
	 * @return
	 */
	private RealMatrix interRatesCorrelation() {
		return interRatesCorrelation(defaultRiskWeights);
	}

	/**
	 * Block matrix for inter rate index correlations
	 * 
	 * @param riskWeights
	 * @return
	 */
	private RealMatrix interRatesCorrelation(List<RiskWeight> riskWeights) {
		RealMatrix corr = new Array2DRowRealMatrix(riskWeights.size(), riskWeights.size());
		for (int i = 0; i < riskWeights.size(); i++) {
			for (int j = 0; j < riskWeights.size(); j++) {
				if (i == j) {
					corr.setEntry(i, i, 1);
				} else if (i < j) {
					corr.setEntry(i, j, calculateRatesCorrelation(riskWeights.get(i), riskWeights.get(j)));
				} else {
					corr.setEntry(i, j, corr.getEntry(j, i));
				}
			}
		}
		return corr.scalarMultiply(interRateCorrThetaMultiplier);
	}

	private double calculateRatesCorrelation(RiskWeight r1, RiskWeight r2) {
		return Math.max(Math.exp(
				-irCorrTheta * (Math.abs(r1.getVertex() - r2.getVertex()) / Math.min(r1.getVertex(), r2.getVertex()))),
				0.4);
	}
}
