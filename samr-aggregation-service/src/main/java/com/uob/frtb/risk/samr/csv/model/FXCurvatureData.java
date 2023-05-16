package com.uob.frtb.risk.samr.csv.model;

import com.uob.frtb.risk.samr.model.RiskClassAndSensitivity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class FXCurvatureData extends CurvatureData {

	private static final long serialVersionUID = 7673797132168234029L;

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return RiskClassAndSensitivity.FX_CURVATURE;
	}
}
