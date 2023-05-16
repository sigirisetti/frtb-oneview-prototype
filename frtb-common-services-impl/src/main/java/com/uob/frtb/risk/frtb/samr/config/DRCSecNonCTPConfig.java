package com.uob.frtb.risk.frtb.samr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import lombok.Getter;

import static com.uob.frtb.risk.frtb.samr.config.ConfigUtil.populateWithStringKeyAndDoubleValues;

@Getter
@Component
public class DRCSecNonCTPConfig {

	@Value("${samr.drc.sec.non.ctp.issuer.rating:AAA,AA,A,BBB,BB,B,CCC,DEF}")
	private String drcIssuerRatingConfig;

	@Value("${samr.drc.sec.non.ctp.senior.rating.rw:0.012,0.020,0.040,0.072,0.128,0.248,0.368,1.000}")
	private String drcSeniorRatingRw;

	@Value("${samr.drc.sec.non.ctp.non.senior.rating.rw:0.012,0.024,0.064,0.176,0.496,0.840,1.000,1.000}")
	private String drcNonSeniorRatingRw;

	private List<String> creditRatings = new ArrayList<>();
	private Map<String, Double> drcSeniorRatingRwMap = new HashMap<>();
	private Map<String, Double> drcNonSeniorRatingRwMap = new HashMap<>();
	
	@PostConstruct
	private void init() {
		for (String s : drcIssuerRatingConfig.split(",")) {
			creditRatings.add(s);
		}
		populateWithStringKeyAndDoubleValues(creditRatings, drcSeniorRatingRw, drcSeniorRatingRwMap);
		populateWithStringKeyAndDoubleValues(creditRatings, drcNonSeniorRatingRw, drcNonSeniorRatingRwMap);
	}

}
