package com.uob.frtb.risk.frtb.samr.results;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IRDeltaTradeLevelResults extends TradeResults {
	private static final long serialVersionUID = 1883210602449802174L;
	private double _3M;
	private double _6M;
	private double _9M;
	private double _1Y;
	private double _2Y;
	private double _3Y;
	private double _5Y;
	private double _7Y;
	private double _10Y;
	private double _15Y;
	private double _30Y;

	@Override
	public List<String> headers() {
		return Arrays.asList("Trade Identifier", "Kb", "Kb Base", "Sb", "Sb Base", "3M", "6M", "9M", "1Y", "2Y", "3Y",
				"5Y", "7Y", "10Y", "15Y", "30Y");
	}

	@Override
	public List<Object> data() {
		return Arrays.asList(getTradeIdentifier(), getKb(), getKbBase(), getSb(), getSbBase(), get_3M(), get_6M(),
				get_9M(), get_1Y(), get_2Y(), get_3Y(), get_5Y(), get_7Y(), get_10Y(), get_15Y(), get_30Y());
	}
}
