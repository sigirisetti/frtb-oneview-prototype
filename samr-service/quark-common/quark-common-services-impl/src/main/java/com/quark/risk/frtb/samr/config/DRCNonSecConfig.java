package com.quark.risk.frtb.samr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import lombok.Getter;

import static com.quark.risk.frtb.samr.config.ConfigUtil.populateWithStringKeyAndDoubleValues;

@Getter
@Component
public class DRCNonSecConfig {

	@Value("${samr.drc.non.sec.issuer.rating:AAA,AA,A,BBB,BB,B,CCC,NR,DEF}")
	private String drcIssuerRatingConfig;

	@Value("${samr.drc.non.sec.issuer.rating.rw:0.005,0.02,0.03,0.06,0.15,0.3,0.5,0.15,1}")
	private String drcIssuerRatingRwConfig;

	@Value("${samr.drc.non.sec.seniority.lgd:SENIOR,JUNIOR,COVERED}")
	private String drcSeniority;

	@Value("${samr.drc.non.sec.seniority.lgd:0.75,1,0.25}")
	private String drcSeniorityLgd;

	private List<String> creditRatings = new ArrayList<>();
	private Map<String, Double> ratingRwMap = new HashMap<>();
	private Map<String, Double> seniorityLgdMap = new HashMap<>();

	@PostConstruct
	private void init() {
		for (String s : drcIssuerRatingConfig.split(",")) {
			creditRatings.add(s);
		}
		populateWithStringKeyAndDoubleValues(creditRatings, drcIssuerRatingRwConfig, ratingRwMap);
		populateWithStringKeyAndDoubleValues(drcSeniority, drcSeniorityLgd, seniorityLgdMap);
	}
}
