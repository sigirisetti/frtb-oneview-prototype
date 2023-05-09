package com.quark.risk.saccr.results;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Counterparty implements Serializable {

	private static final long serialVersionUID = 7523249040053458083L;

	private Map<AssetClass, Currency> assetClasses = new HashMap<>();

	public void addTrade(Trade t) {
		if(!assetClasses.containsKey(t.getAssetClass())) {
		}
	}
}
