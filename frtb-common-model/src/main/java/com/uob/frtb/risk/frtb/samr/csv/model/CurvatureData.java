package com.uob.frtb.risk.frtb.samr.csv.model;

import com.uob.frtb.risk.frtb.samr.model.TradeSensitivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public abstract class CurvatureData extends SAMRData<TradeSensitivity> {

	private static final long serialVersionUID = 1L;

	protected String mtmBase;
	protected String mtmUpShock;
	protected String mtmDownShock;
	protected String deltaShock;

	@Override
	public List<TradeSensitivity> buildTradeData() {
		validateTradeIdAndCurrency();

		if (!isNumber(mtmBase)) {
			addMessage("MTM Base is not a number");
		}
		if (!isNumber(mtmUpShock)) {
			addMessage("MTM Up Shock is not a number");
		}
		if (!isNumber(mtmDownShock)) {
			addMessage("MTM Down Shock is not a number");
		}
		if (!isNumber(deltaShock)) {
			addMessage("Delta is not a number");
		}

		if (getMessages() != null) {
			return Collections.emptyList();
		}

		List<TradeSensitivity> sensitivities = new ArrayList<>();

		sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
				getDouble(mtmBase), getDouble(mtmUpShock), getDouble(mtmDownShock), getDouble(deltaShock)));

		return sensitivities;
	}

}
