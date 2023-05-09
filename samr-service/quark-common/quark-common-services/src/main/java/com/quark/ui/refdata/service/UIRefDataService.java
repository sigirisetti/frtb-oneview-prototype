package com.quark.ui.refdata.service;

import com.quark.marketdata.curve.CurveUnderlying;
import com.quark.ui.refdata.*;

import java.util.Set;

public interface UIRefDataService<T extends CurveUnderlying> {

	SwapTradeWindowRefData getSwapTradeWindowRefData() throws ClassNotFoundException;

	QuotesWindowRefData getQuotesWindowRefData() throws ClassNotFoundException;

	CurveDefWindowRefData getCurveDefWindowRefData(Set<String> hashSet);

	CUDefinitionWindowRefData<T> getCUDefinitionWindowRefData(Set<String> refTypes, Class<T> typ);
	
	PricingEnginesConfigWindowRefData getPricingEngineConfigRefData(Set<String> refTypes);

	PricingContextConfigWindowRefData getPricingContextRefData(Set<String> refTypes); 
	
	MarketDataSetWindowRefData getMarketDataSetWindowRefData(Set<String> refTypes);
}
