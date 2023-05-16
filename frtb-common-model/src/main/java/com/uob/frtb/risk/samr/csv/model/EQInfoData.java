package com.uob.frtb.risk.samr.csv.model;

import com.uob.frtb.risk.samr.model.EquityInfo;
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
public class EQInfoData extends SAMRData<EquityInfo> {

	private static final long serialVersionUID = 1L;

	private String uniqueIndexKey;
	private String bucket;
	private String product;
	private String issuer;
	private String repo;
	private String marketCap;
	private String economy;
	private String sector;

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return null;
	}

	@Override
	public List<EquityInfo> buildTradeData() {

		validateTradeIdAndCurrency();
		if (!isNumber(getBucket())) {
			addMessage("Equity bucket is not specified");
		}
		if (StringUtils.isBlank(getProduct())) {
			addMessage("Product type is not specified");
		}
		if (StringUtils.isBlank(getIssuer())) {
			addMessage("Issuer is not specified");
		}
		if (StringUtils.isBlank(getMarketCap())) {
			addMessage("Market Cap is not specified");
		}
		if (StringUtils.isBlank(getEconomy())) {
			addMessage("Economy is not specified");
		}
		if (StringUtils.isBlank(getSector())) {
			addMessage("Sector is not specified");
		}
		if (getMessages() != null) {
			return Collections.emptyList();
		}

		EquityInfo eqInfo = new EquityInfo();
		eqInfo.setTradeIdentifier(getTradeIdentifier());
		eqInfo.setCurrency(getCurrency());
		eqInfo.setBucket(new Integer(getBucket()));
		eqInfo.setProduct(getProduct());
		eqInfo.setIssuer(getIssuer());
		eqInfo.setMarketCap(getMarketCap());
		eqInfo.setEconomy(getEconomy());
		eqInfo.setSector(getSector());
		eqInfo.setRepo("Y".equalsIgnoreCase(getRepo()));
		return Arrays.asList(eqInfo);
	}
}
