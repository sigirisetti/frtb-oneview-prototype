package com.quark.risk.frtb.samr.csv.model;

import com.quark.risk.frtb.samr.model.RiskClassAndSensitivity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class FXVegaData extends VegaData {

	private static final long serialVersionUID = -748041601005634740L;

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return RiskClassAndSensitivity.FX_VEGA;
	}
}
