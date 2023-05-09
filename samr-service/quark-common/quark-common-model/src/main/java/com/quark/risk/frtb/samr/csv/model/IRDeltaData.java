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
public class IRDeltaData extends SAMRData<TradeSensitivity> {

	private static final long serialVersionUID = 1L;

	private String indexName;
	private String _3M;
	private String _6M;
	private String _1Y;
	private String _2Y;
	private String _3Y;
	private String _5Y;
	private String _7Y;
	private String _10Y;
	private String _15Y;
	private String _30Y;
	private String Inflation;
	private String Basis;

	@Override
	public List<TradeSensitivity> buildTradeData() {
		validateTradeIdAndCurrency();

		if (StringUtils.isBlank(getIndexName())) {
			addMessage("Index is empty");
		}
		if (getMessages() != null) {
			return null;
		}

		List<TradeSensitivity> sensitivities = new ArrayList<>();
		if (isNumber(_3M)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getIndexName(),
					getCurrency(), "3M", getDouble(_3M)));
		} else if (StringUtils.isNotBlank(_3M)) {
			addMessage("3M Vega is not a number");
		}
		if (isNumber(_6M)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getIndexName(),
					getCurrency(), "6M", getDouble(_6M)));
		} else if (StringUtils.isNotBlank(_6M)) {
			addMessage("6M Vega is not a number");
		}
		if (isNumber(_1Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getIndexName(),
					getCurrency(), "1Y", getDouble(_1Y)));
		} else if (StringUtils.isNotBlank(_1Y)) {
			addMessage("1Y Vega is not a number");
		}
		if (isNumber(_2Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getIndexName(),
					getCurrency(), "2Y", getDouble(_2Y)));
		} else if (StringUtils.isNotBlank(_2Y)) {
			addMessage("2Y Vega is not a number");
		}
		if (isNumber(_3Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getIndexName(),
					getCurrency(), "3Y", getDouble(_3Y)));
		} else if (StringUtils.isNotBlank(_3Y)) {
			addMessage("3Y Vega is not a number");
		}
		if (isNumber(_5Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getIndexName(),
					getCurrency(), "5Y", getDouble(_5Y)));
		} else if (StringUtils.isNotBlank(_5Y)) {
			addMessage("5Y Vega is not a number");
		}
		if (isNumber(_7Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getIndexName(),
					getCurrency(), "7Y", getDouble(_7Y)));
		} else if (StringUtils.isNotBlank(_7Y)) {
			addMessage("7Y Vega is not a number");
		}
		if (isNumber(_10Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getIndexName(),
					getCurrency(), "10Y", getDouble(_10Y)));
		} else if (StringUtils.isNotBlank(_10Y)) {
			addMessage("10Y Vega is not a number");
		}
		if (isNumber(_15Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getIndexName(),
					getCurrency(), "15Y", getDouble(_15Y)));
		} else if (StringUtils.isNotBlank(_15Y)) {
			addMessage("15Y Vega is not a number");
		}
		if (isNumber(_30Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getIndexName(),
					getCurrency(), "30Y", getDouble(_30Y)));
		} else if (StringUtils.isNotBlank(_30Y)) {
			addMessage("30Y Vega is not a number");
		}
		if (sensitivities.isEmpty()) {
			addMessage("Deltas not specified for trade id : " + getTradeIdentifier());
		}
		return sensitivities;
	}

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return RiskClassAndSensitivity.IR_DELTA;
	}
}
