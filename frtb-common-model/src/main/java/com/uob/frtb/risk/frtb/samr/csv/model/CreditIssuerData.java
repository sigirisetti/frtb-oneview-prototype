package com.uob.frtb.risk.frtb.samr.csv.model;

import com.uob.frtb.risk.frtb.samr.model.CreditIssuerInfo;
import com.uob.frtb.risk.frtb.samr.model.RiskClassAndSensitivity;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class CreditIssuerData extends SAMRData<CreditIssuerInfo> {

	private static final long serialVersionUID = -8459012804347249095L;

	private String bucket;
	private String issuer;
	private String bondOrCds;
	private String rating;
	private String sector;

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return null;
	}

	@Override
	public List<CreditIssuerInfo> buildTradeData() {

		validateTradeIdAndCurrency();
		if (!NumberUtils.isNumber(getBucket())) {
			addMessage("Issuer bucket is not specified");
		}
		if (StringUtils.isBlank(getIssuer())) {
			addMessage("Issuer is not specified");
		}
		if (StringUtils.isBlank(getBondOrCds())) {
			addMessage("Bond or CDS is not specified");
		}
		if (StringUtils.isBlank(getRating()) && new Integer(getBucket()) != 25) {
			addMessage("Rating is not specified");
		}
		if (StringUtils.isBlank(getSector())) {
			addMessage("Sector is not specified");
		}
		if (getMessages() != null) {
			return Collections.emptyList();
		}

		CreditIssuerInfo issuerInfo = new CreditIssuerInfo();
		issuerInfo.setTradeIdentifier(getTradeIdentifier());
		issuerInfo.setCurrency(getCurrency());
		issuerInfo.setBucket(new Integer(getBucket()));
		issuerInfo.setIssuer(getIssuer());
		issuerInfo.setBondOrCds(getBondOrCds());
		issuerInfo.setRating(getRating());
		issuerInfo.setSector(getSector());
		return Arrays.asList(issuerInfo);
	}
}
