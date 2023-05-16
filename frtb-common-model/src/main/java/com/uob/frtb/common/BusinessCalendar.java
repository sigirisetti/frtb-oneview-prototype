package com.uob.frtb.common;

import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;

public class BusinessCalendar implements Serializable {

	private static final long serialVersionUID = 1L;

	private Calendar calendar;

	public BusinessCalendar() {
		calendar = Calendar.getInstance();
	}

	public BusinessCalendar(TimeZone tz) {
		calendar = Calendar.getInstance(tz);
	}

}
