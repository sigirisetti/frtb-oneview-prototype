package com.uob.frtb.risk.samr.csv.model;

import com.uob.frtb.risk.samr.model.TradeSensitivity;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class VegaData extends SAMRData<TradeSensitivity> {

	private static final long serialVersionUID = 1L;

	protected String _6M;
	protected String _1Y;
	protected String _3Y;
	protected String _5Y;
	protected String _10Y;

	public List<TradeSensitivity> buildTradeData() {
		validateTradeIdAndCurrency();
		if (getMessages() != null) {
			return null;
		}
		List<TradeSensitivity> sensitivities = new ArrayList<>();
		if (isNumber(_6M)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"6M", getDouble(_6M)));
		} else if (StringUtils.isNotBlank(_6M)) {
			addMessage("6M Vega is not a number");
		}
		if (isNumber(_1Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"1Y", getDouble(_1Y)));
		} else if (StringUtils.isNotBlank(_1Y)) {
			addMessage("1Y Vega is not a number");
		}
		if (isNumber(_3Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"3Y", getDouble(_3Y)));
		} else if (StringUtils.isNotBlank(_3Y)) {
			addMessage("3Y Vega  is not a number");
		}
		if (isNumber(_5Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"5Y", getDouble(_5Y)));
		} else if (StringUtils.isNotBlank(_5Y)) {
			addMessage("5Y Vega  is not a number");
		}
		if (isNumber(_10Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"10Y", getDouble(_10Y)));
		} else if (StringUtils.isNotBlank(_10Y)) {
			addMessage("10Y Vega is not a number");
		}
		if (sensitivities.isEmpty()) {
			addMessage("Vegas not specified for trade id : " + getTradeIdentifier());
		}
		return sensitivities;
	}
}
