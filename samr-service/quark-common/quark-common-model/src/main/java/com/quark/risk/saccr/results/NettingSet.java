package com.quark.risk.saccr.results;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NettingSet implements Serializable {

	private static final long serialVersionUID = -2456876136346706118L;

	private List<HedgingSet> hedgingSets;
	
	private List<Trade> trades;
}
