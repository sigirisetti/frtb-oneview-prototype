package com.quark.risk.frtb.samr.csv.model;

import com.quark.risk.frtb.samr.model.RiskClassAndSensitivity;
import com.quark.risk.frtb.samr.model.TradeSensitivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class FXDeltaData extends SAMRData<TradeSensitivity> {

	private static final long serialVersionUID = 1L;

	private String delta;

	@Override
	public List<TradeSensitivity> buildTradeData() {
		validateTradeIdAndCurrency();

		if (getMessages() != null) {
			return Collections.emptyList();
		}

		List<TradeSensitivity> sensitivities = new ArrayList<>();

		if (isNumber(delta)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					getDouble(delta)));
		} else {
			addMessage("FX Delta is not a number");
		}
		return sensitivities;
	}

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return RiskClassAndSensitivity.FX_DELTA;
	}
}
