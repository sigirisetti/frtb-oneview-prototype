package com.uob.frtb.risk.frtb.samr.csv.model;

import com.uob.frtb.risk.frtb.samr.model.RiskClassAndSensitivity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class CRNonSecCurvatureData extends CurvatureData {
	
	private static final long serialVersionUID = -4529108677962708639L;

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return RiskClassAndSensitivity.CR_NS_CURVATURE;
	}
}
