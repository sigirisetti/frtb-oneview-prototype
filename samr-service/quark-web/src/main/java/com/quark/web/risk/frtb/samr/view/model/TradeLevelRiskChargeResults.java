package com.quark.web.risk.frtb.samr.view.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradeLevelRiskChargeResults {
	private String tradeIdentifier;
	private double totalRiskCharge;
	private String details;
}
