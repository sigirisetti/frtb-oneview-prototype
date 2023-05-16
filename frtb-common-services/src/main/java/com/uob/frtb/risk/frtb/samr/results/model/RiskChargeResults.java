package com.uob.frtb.risk.frtb.samr.results.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiskChargeResults {
	private double totalRiskCharge;
	private String currency;
	private List<Result> results;
}
