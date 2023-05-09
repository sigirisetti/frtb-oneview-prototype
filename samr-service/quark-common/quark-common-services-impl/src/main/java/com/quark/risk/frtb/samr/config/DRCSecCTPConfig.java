package com.quark.risk.frtb.samr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import lombok.Getter;
import lombok.Setter;

import static com.quark.risk.frtb.samr.config.ConfigUtil.populateWithStringKeyAndDoubleValues;

@Getter
@Setter
@Component
public class DRCSecCTPConfig {

	@Value("${samr.drc.sec.ctp.issuer.rating:AAA,AA,A,BBB,BB,B,CCC,NR,DEF}")
	private String drcIssuerRatingConfig;

	@Value("${samr.drc.sec.ctp.rating.rw:0.01,0.02,0.03,0.06,0.15,0.30,0.50,0.15,1.00}")
	private String drcRatingRw;

	private List<String> creditRatings = new ArrayList<>();
	private Map<String, Double> drcRatingRwMap = new HashMap<>();

	@PostConstruct
	private void init() {
		for (String s : drcIssuerRatingConfig.split(",")) {
			creditRatings.add(s);
		}
		populateWithStringKeyAndDoubleValues(creditRatings, drcRatingRw, drcRatingRwMap);
	}

}
