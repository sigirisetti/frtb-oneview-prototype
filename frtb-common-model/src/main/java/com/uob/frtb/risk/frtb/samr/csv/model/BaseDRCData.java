package com.uob.frtb.risk.frtb.samr.csv.model;

import com.uob.frtb.risk.frtb.samr.model.BaseDRCEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public abstract class BaseDRCData<T extends BaseDRCEntity> extends SAMRData<T> {

	private static final long serialVersionUID = 1L;

	private Integer bucket;
	private String product;
	private String seniority;
	private String rating;
	private String longOrShort;
	private double maturity;
	private double notional;
	private double mtm;

	protected void setBaseDRCData(BaseDRCEntity e) {
		e.setSensitivityType(getRiskClassAndSensitivity());
		e.setTradeIdentifier(tradeIdentifier);
		e.setCurrency(currency);
		e.setBucket(bucket);
		e.setProduct(product);
		e.setRating(rating);
		e.setLongOrShort(longOrShort);
		e.setMaturity(maturity);
		e.setNotional(notional);
		e.setMtm(mtm);
	}
}
