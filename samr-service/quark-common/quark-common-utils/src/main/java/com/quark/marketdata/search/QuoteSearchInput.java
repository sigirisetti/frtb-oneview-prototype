package com.quark.marketdata.search;

import java.io.Serializable;
import java.util.Date;

public class QuoteSearchInput implements Serializable {

	private static final long serialVersionUID = 2425339706127900687L;

	private String quoteSet;
	private String quoteName;
	private boolean caseSensitive;
	private boolean exactMatch;
	private boolean generateMissing;
	private Date from;
	private Date to;

	public String getQuoteSet() {
		return quoteSet;
	}

	public void setQuoteSet(String quoteSet) {
		this.quoteSet = quoteSet;
	}

	public String getQuoteName() {
		return quoteName;
	}

	public void setQuoteName(String quoteName) {
		this.quoteName = quoteName;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public boolean isExactMatch() {
		return exactMatch;
	}

	public void setExactMatch(boolean exactMatch) {
		this.exactMatch = exactMatch;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public boolean isGenerateMissing() {
		return generateMissing;
	}

	public void setGenerateMissing(boolean generateMissing) {
		this.generateMissing = generateMissing;
	}
}
