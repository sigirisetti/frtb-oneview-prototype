package com.quark.risk.saccr.csv.model;

import com.quark.risk.saccr.model.SaccrMarketQuote;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarketData extends SACCRData<SaccrMarketQuote> {

	private static final long serialVersionUID = -3839086165074710636L;

	private String quoteName;

	private double quoteValue;

	public SaccrMarketQuote build() {
		SaccrMarketQuote md = new SaccrMarketQuote();
		md.setQuoteName(quoteName);
		md.setQuoteValue(quoteValue);
		return md;
	}
}
