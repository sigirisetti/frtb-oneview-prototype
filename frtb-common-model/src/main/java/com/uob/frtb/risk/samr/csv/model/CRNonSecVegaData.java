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
public class CRNonSecVegaData extends SAMRData<TradeSensitivity> {

	private static final long serialVersionUID = -5239759821929900087L;

	private String _6M;
	private String _1Y;
	private String _3Y;
	private String _5Y;
	private String _10Y;

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return RiskClassAndSensitivity.CR_NS_VEGA;
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
		}
		if (isNumber(_1Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), null,
					getCurrency(), "1Y", getDouble(_1Y)));
		}
		if (isNumber(_3Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), null,
					getCurrency(), "3Y", getDouble(_3Y)));
		}
		if (isNumber(_5Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), null,
					getCurrency(), "5Y", getDouble(_5Y)));
		}
		if (isNumber(_10Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), null,
					getCurrency(), "10Y", getDouble(_10Y)));
		}
		if (sensitivities.isEmpty()) {
			addMessage("Vegas not specified for trade id : " + getTradeIdentifier());
		}
		return sensitivities;
	}

}
