package com.uob.frtb.risk.samr.csv.model;

import com.uob.frtb.risk.samr.model.SamrMarketQuote;
import com.uob.frtb.risk.samr.model.RiskClassAndSensitivity;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class MarketData extends SAMRData<SamrMarketQuote> {

	private static final long serialVersionUID = 1L;
	private String timeStamp;
	private String quote;
	private String value;

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return null;
	}

	@Override
	public List<SamrMarketQuote> buildTradeData() {
		if (StringUtils.isBlank(getQuote())) {
			addMessage("Quote name is empty");
		}
		if (!isNumber(value)) {
			addMessage("Quote value is not numeric");
		}
		if (getMessages() != null) {
			return Collections.emptyList();
		}
		SamrMarketQuote md = new SamrMarketQuote();
		md.setQuoteName(getQuote());
		md.setQuoteValue(getDouble(value));
		return Arrays.asList(md);
	}
}
