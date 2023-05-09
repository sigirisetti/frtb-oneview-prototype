package com.quark.risk.frtb.samr.calculators;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class TestMatrixScalarOpImmutability {
	public static void main(String[] args) {
		RealMatrix m = new Array2DRowRealMatrix(new double[][] { { 1, 2 }, { 3, 4 } });
		System.out.println(m.scalarAdd(10));
		System.out.println(m.scalarMultiply(2));
	}
}
