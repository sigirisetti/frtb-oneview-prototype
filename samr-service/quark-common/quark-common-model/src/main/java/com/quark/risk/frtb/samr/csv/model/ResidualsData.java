package com.quark.risk.frtb.samr.csv.model;

import com.quark.risk.frtb.samr.model.Residuals;
import com.quark.risk.frtb.samr.model.RiskClassAndSensitivity;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true, includeFieldNames = true)
public class ResidualsData extends SAMRData<Residuals> {

	private static final long serialVersionUID = 3697925205455387930L;

	private double notional;
	private int type;

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return RiskClassAndSensitivity.RESIDUALS;
	}

	@Override
	public List<Residuals> buildTradeData() {
		return Arrays.asList(buildResiduals());
	}

	public Residuals buildResiduals() {
		validateTradeIdAndCurrency();
		if (getMessages() != null) {
			return null;
		}

		Residuals e = new Residuals();
		e.setSensitivityType(getRiskClassAndSensitivity());
		e.setTradeIdentifier(tradeIdentifier);
		e.setCurrency(getCurrency());
		e.setNotional(getNotional());
		e.setType(getType());
		return e;
	}
}
