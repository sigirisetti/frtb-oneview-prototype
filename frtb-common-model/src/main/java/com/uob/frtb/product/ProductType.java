package com.uob.frtb.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum ProductType {

	VANILLA_SWAP("Vanilla Swap"), BASIS_SWAP("Basis Swap"), CROSS_CURRENCY_SWAP("Cross Currency Swap"), SWAPTION(
			"Swaption"), BOND("Bond"), FX_FORWARD("FX Forward"), CAP(
					"Cap"), FLOOR("Floor"), COLLAR("Collar"), CDS("Credit Default Swap"), CDS_INDEX("CDS Index");

	private static List<String> typeList;

	private String productType;

    ProductType(String t) {
		this.productType = t;
	}

	public String getProductType() {
		return productType;
	}

	public static List<String> getProductTypeList() {
		if (typeList != null) {
			return typeList;
		}
		ProductType[] types = ProductType.values();
		typeList = new ArrayList<>();
		for (int i = 0; i < types.length; i++) {
			typeList.add(types[i].getProductType());
		}
		Collections.sort(typeList);
		return typeList;
	}
}
