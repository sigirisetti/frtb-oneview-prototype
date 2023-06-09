package com.uob.frtb.risk.samr.csv.model;

import com.uob.frtb.risk.samr.model.RiskClassAndSensitivity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class EQVegaData extends VegaData {

	private static final long serialVersionUID = -277883722213604280L;

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return RiskClassAndSensitivity.EQ_VEGA;
	}
}
