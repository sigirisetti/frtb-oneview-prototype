package com.uob.frtb.risk.samr.csv.model;

import com.uob.frtb.risk.samr.model.RiskClassAndSensitivity;
import com.uob.frtb.risk.samr.model.TradeSensitivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class EQDeltaData extends SAMRData<TradeSensitivity> {
	private static final long serialVersionUID = 1L;
	private String repoIndicator;
	private String value;

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return RiskClassAndSensitivity.EQ_DELTA;
	}

	@Override
	public List<TradeSensitivity> buildTradeData() {
		validateTradeIdAndCurrency();

		if (getMessages() != null) {
			return Collections.emptyList();
		}

		List<TradeSensitivity> sensitivities = new ArrayList<>();

		if (isNumber(value)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					getDouble(value)));
		} else {
			addMessage("EQ Delta is not a number");
		}
		return sensitivities;
	}
}
