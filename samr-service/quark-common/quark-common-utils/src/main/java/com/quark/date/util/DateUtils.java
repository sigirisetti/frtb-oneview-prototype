package com.quark.date.util;

import org.apache.poi.ss.usermodel.DateUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

	private static final double CAL_1900_ABSOLUTE = (double) absoluteDay(
			new GregorianCalendar(1900, Calendar.JANUARY, 1)) - 2.0;

	public static double getExcelDate(Date date) {
		Calendar calStart = new GregorianCalendar();
		calStart.setTime(date);
		calStart = resetTimeFields(calStart);
		return (double) absoluteDay(calStart) - CAL_1900_ABSOLUTE;

	}

	private static Calendar resetTimeFields(final Calendar cal) {
		cal.get(Calendar.HOUR_OF_DAY);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.get(Calendar.HOUR_OF_DAY);
		return cal;
	}

	private static int absoluteDay(Calendar cal) {
		return cal.get(Calendar.DAY_OF_YEAR) + daysInPriorYears(cal.get(Calendar.YEAR));
	}

	private static int daysInPriorYears(int yr) {
		if (yr < 1601) {
			throw new IllegalArgumentException("'year' must be 1601 or greater");
		}
		int y = yr - 1601;
		return 365 * y + y / 4 - y / 100 + y / 400;
	}

    public static void main(String[] args) {
		System.out.println(getExcelDate(new Date()));
		System.out.println(DateUtil.getExcelDate(new Date()));
		System.out.println(DateUtil.getJavaDate(42576));
	}
}
