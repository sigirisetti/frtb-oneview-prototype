package com.quark.risk.frtb.samr.csv.model;

import com.quark.risk.frtb.samr.model.RiskClassAndSensitivity;
import com.quark.risk.frtb.samr.model.TradeSensitivity;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class CommodityDeltaData extends SAMRData<TradeSensitivity> {

	private static final long serialVersionUID = 1L;

	private String spot;
	private String _3M;
	private String _6M;
	private String _1Y;
	private String _2Y;
	private String _3Y;
	private String _5Y;
	private String _10Y;
	private String _15Y;
	private String _20Y;
	private String _30Y;
	private String Inflation;
	private String Basis;

	@Override
	public List<TradeSensitivity> buildTradeData() {
		validateTradeIdAndCurrency();
		if (StringUtils.isBlank(getSpot())) {
			addMessage("Spot price is empty");
		}
		if (getMessages() != null) {
			return null;
		}

		List<TradeSensitivity> sensitivities = new ArrayList<>();
		sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(), "0D",
				getDouble(spot)));

		if (isNumber(_3M)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"3M", getDouble(_3M)));
		}
		if (isNumber(_6M)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"6M", getDouble(_6M)));
		}
		if (isNumber(_1Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"1Y", getDouble(_1Y)));
		}
		if (isNumber(_2Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"2Y", getDouble(_2Y)));
		}
		if (isNumber(_3Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"3Y", getDouble(_3Y)));
		}
		if (isNumber(_5Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"5Y", getDouble(_5Y)));
		}
		if (isNumber(_10Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"10Y", getDouble(_10Y)));
		}
		if (isNumber(_15Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"15Y", getDouble(_15Y)));
		}
		if (isNumber(_20Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"20Y", getDouble(_20Y)));
		}
		if (isNumber(_30Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"30Y", getDouble(_30Y)));
		}
		if (sensitivities.isEmpty()) {
			addMessage("Deltas not specified for trade id : " + getTradeIdentifier());
		}
		return sensitivities;
	}

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return RiskClassAndSensitivity.COMM_DELTA;
	}
}
