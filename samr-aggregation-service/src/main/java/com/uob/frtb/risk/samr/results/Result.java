package com.uob.frtb.risk.samr.results;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Result {

	private String parentName;
	private String nodeName;
	private boolean showTradeResults;
	private double amountLowCorr;
	private double amountBaseLowCorr;
	private double amount;
	private double amountBase;
	private double amountHighCorr;
	private double amountBaseHighCorr;

	@Getter(AccessLevel.NONE)
	private Result parent;
	private List<Result> children = new ArrayList<>();

	public Result(String parentName, String nodeName, boolean showTradeResults, double amountLowCorr,
			double amountBaseLowCorr, double amount, double amountBase, double amountHighCorr,
			double amountBaseHighCorr) {
		this.parentName = parentName;
		this.nodeName = nodeName;
		this.showTradeResults = showTradeResults;
		this.amountLowCorr = amountLowCorr;
		this.amountBaseLowCorr = amountBaseLowCorr;
		this.amount = amount;
		this.amountBase = amountBase;
		this.amountHighCorr = amountHighCorr;
		this.amountBaseHighCorr = amountBaseHighCorr;
	}

	public void addChild(Result r) {
		r.setParent(this);
		children.add(r);
	}

	public String getIdentifier() {
		if (parent != null) {
			return parent.getIdentifier() + ":" + getNodeName();
		} else {
			return getNodeName();
		}
	}
}
