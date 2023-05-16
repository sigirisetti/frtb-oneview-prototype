package com.uob.frtb.risk.samr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class IRCurvatureConfig {

	@Value("${samr.ir.curvature.bucket.corr:0.25}")
	private double irCurvCurrBucketCorr;

	@Value("${samr.ir.curvature.rw:0.024}")
	private double irCurvatureRw;
	
}
