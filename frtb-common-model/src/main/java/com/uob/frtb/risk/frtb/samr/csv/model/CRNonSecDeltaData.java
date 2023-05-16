package com.uob.frtb.risk.frtb.samr.csv.model;

import com.uob.frtb.risk.frtb.samr.model.RiskClassAndSensitivity;
import com.uob.frtb.risk.frtb.samr.model.TradeSensitivity;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class CRNonSecDeltaData extends SAMRData<TradeSensitivity> {

	private static final long serialVersionUID = -3732645152936792627L;

	private String _6M;
	private String _1Y;
	private String _3Y;
	private String _5Y;
	private String _10Y;

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return RiskClassAndSensitivity.CR_NS_DELTA;
	}

	@Override
	public List<TradeSensitivity> buildTradeData() {
		validateTradeIdAndCurrency();
		if (getMessages() != null) {
			return Collections.emptyList();
		}

		List<TradeSensitivity> sensitivities = new ArrayList<>();

		if (isNumber(_6M)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), null,
					getCurrency(), "6M", getDouble(_6M)));
		} else if (StringUtils.isNotBlank(_6M)) {
			addMessage("6M Vega is not a number");
		}
		if (isNumber(_1Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), null,
					getCurrency(), "1Y", getDouble(_1Y)));
		} else if (StringUtils.isNotBlank(_1Y)) {
			addMessage("1Y Vega is not a number");
		}
		if (isNumber(_3Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), null,
					getCurrency(), "3Y", getDouble(_3Y)));
		} else if (StringUtils.isNotBlank(_3Y)) {
			addMessage("3Y Vega is not a number");
		}
		if (isNumber(_5Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), null,
					getCurrency(), "5Y", getDouble(_5Y)));
		} else if (StringUtils.isNotBlank(_5Y)) {
			addMessage("5Y Vega is not a number");
		}
		if (isNumber(_10Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), null,
					getCurrency(), "10Y", getDouble(_10Y)));
		} else if (StringUtils.isNotBlank(_10Y)) {
			addMessage("10Y Vega is not a number");
		}
		if (sensitivities.isEmpty()) {
			addMessage("Deltas not specified for trade id : " + getTradeIdentifier());
		}
		return sensitivities;
	}
}
