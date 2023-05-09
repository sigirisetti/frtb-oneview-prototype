package com.quark.ui.refdata;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SwapTradeWindowRefData implements Serializable {

	private static final long serialVersionUID = -5766034679226473111L;

	private List<String> businessCalendars;
	private List<String> iborIndexes;
	private List<String> dayCounters;
	private List<String> paymentBDCs;
	private List<String> effectiveDateconventions;
	private List<String> terminationDateConventions;
	private List<String> dateGenerationRule;
}
