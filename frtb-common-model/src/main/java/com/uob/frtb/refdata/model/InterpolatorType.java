package com.uob.frtb.refdata.model;

public enum InterpolatorType {

	BACKWARD_FLAT_INTERPOLATION("BackwardFlatInterpolation"), CONVEX_MONOTONE("ConvexMonotone"), CUBIC_INTERPOLATION(
			"CubicInterpolation"), FORWARD_FLAT_INTERPOLATION("ForwardFlatInterpolation"), LINEAR_INTERPOLATION(
			"LinearInterpolation"), LOG_CUBIC_INTERPOLATION("LogCubicInterpolation"), LOG_LINEAR_INTERPOLATION(
			"LogLinearInterpolation"), SABR_INTERPOLATION("SABRInterpolation");

	private String interpolatorType;

	private InterpolatorType(String t) {
		this.interpolatorType = t;
	}

	public String getInterpolatorType() {
		return interpolatorType;
	}
}
