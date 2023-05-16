package com.uob.frtb.risk.samr.csv.reader;

import com.uob.frtb.refdata.model.Tenor;

public class TermTenorPair {

	private Tenor term;
	private Tenor tenor;
	
	public TermTenorPair(String termDotTenor) {
		String[] s = termDotTenor.split("\\.");
		if (s.length != 2) {
			throw new IllegalArgumentException("Expecting header in format [term].[tenor]");
		}
		this.term = Tenor.parseTenor(s[0]);
		this.tenor = Tenor.parseTenor(s[1]);
	}

	public Tenor getTerm() {
		return term;
	}

	public void setTerm(Tenor term) {
		this.term = term;
	}

	public Tenor getTenor() {
		return tenor;
	}

	public void setTenor(Tenor tenor) {
		this.tenor = tenor;
	}
}
