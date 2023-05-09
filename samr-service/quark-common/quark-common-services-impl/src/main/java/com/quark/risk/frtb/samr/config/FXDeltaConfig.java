package com.quark.risk.frtb.samr.config;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class FXDeltaConfig {

	@Value("${samr.fx.delta.rw:0.3}")
	private double fxDeltaRiskWeight;

	@Value("${samr.fx.delta.rw:0.6}")
	private double fxCorr;

	public RealMatrix getFXCorr(int nCurrencies) {
		RealMatrix corr = new Array2DRowRealMatrix(nCurrencies, nCurrencies);
		for (int i = 0; i < nCurrencies; i++) {
			for (int j = 0; j < nCurrencies; j++) {
				if (i == j) {
					corr.setEntry(i, j, 1);
				} else {
					corr.setEntry(i, j, fxCorr);
				}
			}
		}
		return corr;
	}
}
