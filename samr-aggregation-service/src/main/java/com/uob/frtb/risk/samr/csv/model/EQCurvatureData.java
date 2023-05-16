package com.uob.frtb.risk.samr.csv.model;

import com.uob.frtb.risk.samr.model.RiskClassAndSensitivity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class EQCurvatureData extends CurvatureData {

	private static final long serialVersionUID = -8889534040588026140L;

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return RiskClassAndSensitivity.EQ_CURVATURE;
	}
}
