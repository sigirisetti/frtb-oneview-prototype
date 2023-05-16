package com.uob.frtb.risk.samr.config;

import com.uob.frtb.refdata.model.Tenor;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import lombok.Getter;

@Component
@Getter
public class FXVegaConfig {

	@Value("${samr.vega.rw:0.55}")
	private double vegaRiskWeight;

	@Value("${samr.fx.liquidity.horizon:40}")
	private double fxLiquidityHorizon;

	private double fxVegaRiskWeight;

	@Value("${samr.fx.option.maturity.tenors:0.5,1,3,5,10}")
	private String strOptionMaturityTenors;

	private double[] optionMaturityTenors;
	private int[] optionMaturityTenorCodes;

	@Value("${samr.fx.vega.opt.mat.alpha:0.01}")
	private double fxVegaOptMatAlpha;

	@Value("${samr.fx.vega.cross.bucket.corr:0.6}")
	private double fxCrossBucketCorr;

	private RealMatrix fxVegaCorr;

	@PostConstruct
	private void init() {
		fxVegaRiskWeight = Math.min(vegaRiskWeight * Math.sqrt(fxLiquidityHorizon * 0.1), 1);
		String tokens[] = strOptionMaturityTenors.split(",");
		optionMaturityTenors = new double[tokens.length];
		optionMaturityTenorCodes = new int[tokens.length];
		for (int i = 0; i < tokens.length; i++) {
			optionMaturityTenors[i] = new Double(tokens[i]);
			optionMaturityTenorCodes[i] = (int) (optionMaturityTenors[i] * Tenor.DEFAULT_NUM_DAYS_IN_A_YEAR);
		}
		fxVegaCorr = buildOptionMaturityCorrelation();
	}

	private RealMatrix buildOptionMaturityCorrelation() {
		int dim = optionMaturityTenors.length;
		RealMatrix optMatCorr = new Array2DRowRealMatrix(dim, dim);
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				optMatCorr.setEntry(i, j,
						Math.exp(-fxVegaOptMatAlpha * Math.abs(optionMaturityTenors[i] - optionMaturityTenors[j])
								/ Math.min(optionMaturityTenors[i], optionMaturityTenors[j])));
			}
		}
		return optMatCorr;
	}

}
