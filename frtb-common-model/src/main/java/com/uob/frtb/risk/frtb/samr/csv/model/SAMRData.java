package com.uob.frtb.risk.frtb.samr.csv.model;

import com.uob.frtb.risk.common.model.BaseRiskEntity;
import com.uob.frtb.risk.frtb.samr.model.RiskClassAndSensitivity;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public abstract class SAMRData<T extends BaseRiskEntity> implements Serializable {

	private static final long serialVersionUID = 1L;

	private NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);

	protected String tradeIdentifier;
	protected String currency;
	protected List<String> messages;

	protected abstract RiskClassAndSensitivity getRiskClassAndSensitivity();

	public abstract List<T> buildTradeData();

	public void setTradeIdentifier(String tradeIdentifier) {
		this.tradeIdentifier = tradeIdentifier;
	}

	public void addMessage(String msg) {
		if (messages == null) {
			messages = new ArrayList<>();
		}
		messages.add(msg);
	}

	protected void validateTradeIdAndCurrency() {
		if (StringUtils.isBlank(getTradeIdentifier())) {
			addMessage("Trade identifier is empty");
		}
		if (StringUtils.isBlank(getCurrency())) {
			addMessage("Currency is empty");
		}
	}

	protected boolean isNumber(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}
		try {
			nf.parse(str.trim().replaceAll("[()]", "")).doubleValue();
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	protected double getDouble(String str) {
		try {
			return nf.parse(str.trim().replaceAll("[()]", "")).doubleValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0.0;
	}
}
