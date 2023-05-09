package com.quark.marketdata.curve;

public enum CurveUnderlyingType {

	BASIS_SWAP("BasisSwap"), BOND("Bond"), CDS("CDS"), CDS_INDEX("CDSIndex"), FIXED_COUPON_CDS("FixedCouponCDS"), FRA(
			"FRA"), FUTURE("Future"), FX_FWD_FIXED("FxForwardFixed"), FX_FORWARD_TENOR("FxForwardTenor"), GENERIC_CDS(
			"GenericCDS"), MONEY_MARKET("MoneyMarket"), SWAP("Swap");

	private String type;

	private CurveUnderlyingType(String t) {
		this.type = t;
	}

	public String type() {
		return type;
	}
}
