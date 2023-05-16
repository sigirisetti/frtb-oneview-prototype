package com.uob.frtb.risk.samr.csv.model;

import com.uob.frtb.risk.samr.model.RiskClassAndSensitivity;
import com.uob.frtb.risk.samr.model.TradeSensitivity;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class IRVegaData extends SAMRData<TradeSensitivity> {

	private static final long serialVersionUID = 1L;

	private String _6M_6M;
	private String _6M_1Y;
	private String _6M_3Y;
	private String _6M_5Y;
	private String _6M_10Y;
	private String _1Y_6M;
	private String _1Y_1Y;
	private String _1Y_3Y;
	private String _1Y_5Y;
	private String _1Y_10Y;
	private String _3Y_6M;
	private String _3Y_1Y;
	private String _3Y_3Y;
	private String _3Y_5Y;
	private String _3Y_10Y;
	private String _5Y_6M;
	private String _5Y_1Y;
	private String _5Y_3Y;
	private String _5Y_5Y;
	private String _5Y_10Y;
	private String _10Y_6M;
	private String _10Y_1Y;
	private String _10Y_3Y;
	private String _10Y_5Y;
	private String _10Y_10Y;

	public List<TradeSensitivity> buildTradeData() {
		validateTradeIdAndCurrency();

		if (getMessages() != null) {
			return null;
		}

		List<TradeSensitivity> sensitivities = new ArrayList<TradeSensitivity>();
		if (isNumber(_6M_6M)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"6M", getDouble(_6M_6M), "6M"));
		} else if (StringUtils.isNotBlank(_6M_6M)) {
			addMessage("6M_6M Vega is not a number");
		}
		if (isNumber(_6M_1Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"1Y", getDouble(_6M_1Y), "6M"));
		} else if (StringUtils.isNotBlank(_6M_1Y)) {
			addMessage("6M_1Y Vega is not a number");
		}
		if (isNumber(_6M_3Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"3Y", getDouble(_6M_3Y), "6M"));
		} else if (StringUtils.isNotBlank(_6M_3Y)) {
			addMessage("6M_3Y Vega is not a number");
		}
		if (isNumber(_6M_5Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"5Y", getDouble(_6M_5Y), "6M"));
		} else if (StringUtils.isNotBlank(_6M_5Y)) {
			addMessage("6M_5Y Vega is not a number");
		}
		if (isNumber(_6M_10Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"10Y", getDouble(_6M_10Y), "6M"));
		} else if (StringUtils.isNotBlank(_6M_10Y)) {
			addMessage("6M_10Y Vega is not a number");
		}
		if (isNumber(_1Y_6M)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"6M", getDouble(_1Y_6M), "1Y"));
		} else if (StringUtils.isNotBlank(_1Y_6M)) {
			addMessage("1Y_6M Vega is not a number");
		}
		if (isNumber(_1Y_1Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"1Y", getDouble(_1Y_1Y), "1Y"));
		} else if (StringUtils.isNotBlank(_1Y_1Y)) {
			addMessage("1Y_1Y Vega is not a number");
		}
		if (isNumber(_1Y_3Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"3Y", getDouble(_1Y_3Y), "1Y"));
		} else if (StringUtils.isNotBlank(_1Y_3Y)) {
			addMessage("1Y_3Y Vega is not a number");
		}
		if (isNumber(_1Y_5Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"5Y", getDouble(_1Y_5Y), "1Y"));
		} else if (StringUtils.isNotBlank(_1Y_5Y)) {
			addMessage("1Y_5Y Vega is not a number");
		}
		if (isNumber(_1Y_10Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"10Y", getDouble(_1Y_10Y), "1Y"));
		} else if (StringUtils.isNotBlank(_1Y_10Y)) {
			addMessage("1Y_10Y Vega is not a number");
		}
		if (isNumber(_3Y_6M)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"6M", getDouble(_3Y_6M), "3Y"));
		} else if (StringUtils.isNotBlank(_3Y_6M)) {
			addMessage("3Y_6M Vega is not a number");
		}
		if (isNumber(_3Y_1Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"1Y", getDouble(_3Y_1Y), "3Y"));
		} else if (StringUtils.isNotBlank(_3Y_1Y)) {
			addMessage("3Y_1Y Vega is not a number");
		}
		if (isNumber(_3Y_3Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"3Y", getDouble(_3Y_3Y), "3Y"));
		} else if (StringUtils.isNotBlank(_3Y_3Y)) {
			addMessage("3Y_3Y Vega is not a number");
		}
		if (isNumber(_3Y_5Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"5Y", getDouble(_3Y_5Y), "3Y"));
		} else if (StringUtils.isNotBlank(_3Y_5Y)) {
			addMessage("3Y_5Y Vega is not a number");
		}
		if (isNumber(_3Y_10Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"10Y", getDouble(_3Y_10Y), "3Y"));
		} else if (StringUtils.isNotBlank(_3Y_10Y)) {
			addMessage("3Y_10Y Vega is not a number");
		}
		if (isNumber(_5Y_6M)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"6M", getDouble(_5Y_6M), "5Y"));
		} else if (StringUtils.isNotBlank(_5Y_6M)) {
			addMessage("5Y_6M Vega is not a number");
		}
		if (isNumber(_5Y_1Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"1Y", getDouble(_5Y_1Y), "5Y"));
		} else if (StringUtils.isNotBlank(_5Y_1Y)) {
			addMessage("5Y_1Y Vega is not a number");
		}
		if (isNumber(_5Y_3Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"3Y", getDouble(_5Y_3Y), "5Y"));
		} else if (StringUtils.isNotBlank(_5Y_3Y)) {
			addMessage("5Y_3Y Vega is not a number");
		}
		if (isNumber(_5Y_5Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"5Y", getDouble(_5Y_5Y), "5Y"));
		} else if (StringUtils.isNotBlank(_5Y_5Y)) {
			addMessage("5Y_5Y Vega is not a number");
		}
		if (isNumber(_5Y_10Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"10Y", getDouble(_5Y_10Y), "5Y"));
		} else if (StringUtils.isNotBlank(_5Y_10Y)) {
			addMessage("5Y_10Y Vega is not a number");
		}
		if (isNumber(_10Y_6M)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"6M", getDouble(_10Y_6M), "10Y"));
		} else if (StringUtils.isNotBlank(_10Y_6M)) {
			addMessage("10Y_6M Vega is not a number");
		}
		if (isNumber(_10Y_1Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"1Y", getDouble(_10Y_1Y), "10Y"));
		} else if (StringUtils.isNotBlank(_10Y_1Y)) {
			addMessage("10Y_1Y Vega is not a number");
		}
		if (isNumber(_10Y_3Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"3Y", getDouble(_10Y_3Y), "10Y"));
		} else if (StringUtils.isNotBlank(_10Y_3Y)) {
			addMessage("10Y_3Y Vega is not a number");
		}
		if (isNumber(_10Y_5Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"5Y", getDouble(_10Y_5Y), "10Y"));
		} else if (StringUtils.isNotBlank(_10Y_5Y)) {
			addMessage("10Y_5Y Vega is not a number");
		}
		if (isNumber(_10Y_10Y)) {
			sensitivities.add(new TradeSensitivity(getRiskClassAndSensitivity(), getTradeIdentifier(), getCurrency(),
					"10Y", getDouble(_10Y_10Y), "10Y"));
		} else if (StringUtils.isNotBlank(_10Y_10Y)) {
			addMessage("10Y_10Y Vega is not a number");
		}
		if (sensitivities.isEmpty()) {
			addMessage("Vegas not specified for trade id : " + getTradeIdentifier());
		}

		return sensitivities;
	}

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return RiskClassAndSensitivity.IR_VEGA;
	}
}
