package com.quark.refdata.model;

public enum DayCounterType {

	ACTUAL_360("Actual360"), ACTUAL_365_FIXED("Actual365Fixed"), ACTUAL_365_NO_LEAP("Actual365NoLeap"), ACTUAL_ACTUAL(
			"ActualActual"), BUSINESS_252("Business252"), ONE_DAY_COUNTER("OneDayCounter"), SIMPLE_DAY_COUNTER(
			"SimpleDayCounter"), THIRTY_360("Thirty360");

	private String dayCounter;

	private DayCounterType(String dc) {
		this.dayCounter = dc;
	}

	public String getDayCounter() {
		return dayCounter;
	}
}
