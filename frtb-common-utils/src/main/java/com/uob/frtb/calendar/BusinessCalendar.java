package com.uob.frtb.calendar;

import java.util.Calendar;

public class BusinessCalendar {

	public static Calendar rollTodaysCalendar(int nDays) {
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < nDays;) {
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				cal.add(Calendar.DAY_OF_MONTH, 1);
				continue;
			}
			cal.add(Calendar.DAY_OF_MONTH, 1);
			i++;
		}
		return cal;
	}
}
