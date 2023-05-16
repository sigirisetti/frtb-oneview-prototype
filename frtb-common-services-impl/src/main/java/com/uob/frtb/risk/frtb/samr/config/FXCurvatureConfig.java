package com.uob.frtb.risk.frtb.samr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class FXCurvatureConfig {

	@Value("${samr.fx.curvature.bucket.corr:0.36}")
	private double fxCurvCurrBucketCorr;

	@Value("${samr.fx.curvature.rw:0.3}")
	private double fxCurvatureRw;
}
