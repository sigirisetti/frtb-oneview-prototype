package com.quark.refdata.model;

import java.io.Serializable;

public class Tenor implements Serializable, Comparable<Tenor> {

	private static final long serialVersionUID = 5715403700988770980L;

    public enum TimeUnit {
        D, W, M, Y
    }

	public static final int DEFAULT_NUM_DAYS_IN_A_MONTH = 30;
	public static final int DEFAULT_NUM_DAYS_IN_A_YEAR = 360;
	private int daysInAMonth = DEFAULT_NUM_DAYS_IN_A_MONTH;
	private int daysInAYear = DEFAULT_NUM_DAYS_IN_A_YEAR;

	private int numberOfUnits;
	private TimeUnit timeUnit;

	public Tenor(int numberOfUnits, TimeUnit timeUnit) {
		this.numberOfUnits = numberOfUnits;
		this.timeUnit = timeUnit;
	}

	public int getNumberOfUnits() {
		return numberOfUnits;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public int getNumberOfDays() {
		if (timeUnit == TimeUnit.D) {
			return numberOfUnits;
		} else if (timeUnit == TimeUnit.W) {
			return numberOfUnits * 7;
		} else if (timeUnit == TimeUnit.M) {
			return numberOfUnits * daysInAMonth;
		} else if (timeUnit == TimeUnit.Y) {
			return numberOfUnits * daysInAYear;
		}
		throw new IllegalStateException();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + numberOfUnits;
		result = prime * result + ((timeUnit == null) ? 0 : timeUnit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tenor other = (Tenor) obj;
		if (numberOfUnits != other.numberOfUnits)
			return false;
        return timeUnit == other.timeUnit;
    }

	@Override
	public String toString() {
		return numberOfUnits + "" + timeUnit;
	}

	public static Tenor parseTenor(String tenor) {
		String numUnits = tenor.replaceAll("[^\\d.]", "");
		String timeUnit = tenor.replaceAll("[^DWMY]", "");
		if (numUnits.trim().length() == 0 || timeUnit.trim().length() == 0) {
			throw new IllegalArgumentException("Not a tenor string");
		}
		int i = (int) Math.round(Double.parseDouble(numUnits));
		if (i > 99999) {
			i = 99999;
		}
		String c = timeUnit.substring(0, 1);
		return new Tenor(i, TimeUnit.valueOf(c));
	}

	@Override
	public int compareTo(Tenor o) {
		if (this == o) {
			return 0;
		}
		int n1 = getNumberOfDays();
		int n2 = o.getNumberOfDays();

		if (n1 == n2) {
			return 0;
		} else if (n1 > n2) {
			return -1;
		} else {
			return 1;
		}
	}
}
