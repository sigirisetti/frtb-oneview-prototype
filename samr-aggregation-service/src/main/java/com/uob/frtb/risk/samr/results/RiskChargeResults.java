package com.uob.frtb.risk.samr.results;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RiskChargeResults {
	private double totalRiskCharge;
	private String currency;
	private List<Result> results;
}
