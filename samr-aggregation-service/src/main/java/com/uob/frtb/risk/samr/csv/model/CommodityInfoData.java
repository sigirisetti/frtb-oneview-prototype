package com.uob.frtb.risk.samr.csv.model;

import com.uob.frtb.risk.samr.model.CommodityInfo;
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
public class CommodityInfoData extends SAMRData<CommodityInfo> {

	private static final long serialVersionUID = 1L;

	protected String tradeIdentifier;
	private String bucket;
	private String product;
	private String underlying;
	private String grade;
	private String deliveryLocation;

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return null;
	}

	@Override
	public List<CommodityInfo> buildTradeData() {

		if (StringUtils.isBlank(getTradeIdentifier())) {
			addMessage("Trade identifier is empty");
		}
		if (!isNumber(getBucket())) {
			addMessage("Commodity bucket is not specified");
		}
		if (StringUtils.isBlank(getProduct())) {
			addMessage("Product type is not specified");
		}
		if (StringUtils.isBlank(getUnderlying())) {
			addMessage("Underlying is not specified");
		}
		if (StringUtils.isBlank(getGrade())) {
			addMessage("Grade is not specified");
		}
		if (StringUtils.isBlank(getDeliveryLocation())) {
			addMessage("Delivery Location is not specified");
		}
		if (getMessages() != null) {
			return Collections.emptyList();
		}

		CommodityInfo commInfo = new CommodityInfo();
		commInfo.setTradeIdentifier(getTradeIdentifier());
		commInfo.setBucket(new Integer(getBucket()));
		commInfo.setProduct(getProduct());
		commInfo.setUnderlying(getUnderlying());
		commInfo.setGrade(getGrade());
		commInfo.setDeliveryLocation(getDeliveryLocation());
		return Arrays.asList(commInfo);
	}
}
