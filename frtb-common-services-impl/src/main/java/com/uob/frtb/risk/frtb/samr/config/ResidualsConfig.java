package com.uob.frtb.risk.frtb.samr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class ResidualsConfig {

	@Value("${samr.residuals.type1:0.001}")
	private double type1Factor;
	
	@Value("${samr.residuals.type2:0.01}")
	private double type2Factor;
	
}
