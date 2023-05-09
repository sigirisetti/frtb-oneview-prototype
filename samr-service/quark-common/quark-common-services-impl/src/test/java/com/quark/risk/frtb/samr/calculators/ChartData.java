package com.quark.risk.frtb.samr.calculators;

import java.util.Calendar;

public class ChartData {
	public static void main(String[] args) {
		double a = 0.9;
		double b = 1.1;

		double ir = 16000000;
		double fx = 12000000;
		double eq = 9000000;
		double cr = 26000000;
		double co = 12000000;

		genLineData(a, b, ir);
		System.out.println();
		genLineData(a, b, fx);
		System.out.println();
		genLineData(a, b, eq);
		System.out.println();
		genLineData(a, b, cr);
		System.out.println();
		genLineData(a, b, co);
		System.out.println();

		areaChartData();
	}

	private static void genLineData(double a, double b, double ir) {
		Calendar c = Calendar.getInstance();
		for (int i = 1; i <= 30; i++) {
			if(i<15) {
				ir+=20000;
			}else {
				ir-=20000;
			}
			c.add(Calendar.DAY_OF_MONTH, -1);
			double val = a + (b - a) * Math.random();
			val = val * ir;
			System.out.print(String.format("{x:%d, y:%f}, ", i, val));
		}
	}

	private static void areaChartData() {
		Calendar c = Calendar.getInstance();
		double d = 25000000;
		double a = 0.8;
		double b = 1.2;

		for (int i = 0; i < 30; i++) {
			c.add(Calendar.DAY_OF_MONTH, -1);
			double val = a + (b - a) * Math.random();
			val = val * d;
			System.out.print(String.format("[%d, %f], ", c.getTime().getTime(), val));
		}
	}
}
