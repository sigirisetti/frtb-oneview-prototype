package com.uob.frtb.risk.samr.model;

import com.uob.frtb.refdata.model.Tenor;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RiskWeight implements Serializable {

	private static final long serialVersionUID = 1L;

	private int currencyType;
	private double vertex;
	private double weight;
	private int tenorCode;

	public RiskWeight(int currencyType, double vertex, double weight) {
		this.currencyType = currencyType;
		this.vertex = vertex;
		this.weight = weight;
		if (vertex < 1) {
			this.tenorCode = Tenor.parseTenor("" + vertex * 12 + "M").getNumberOfDays();
		} else {
			this.tenorCode = Tenor.parseTenor("" + vertex + "Y").getNumberOfDays();
		}
	}
}
