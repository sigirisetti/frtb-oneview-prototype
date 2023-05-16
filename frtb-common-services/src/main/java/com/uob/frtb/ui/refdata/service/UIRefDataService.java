package com.uob.frtb.ui.refdata.service;

import com.uob.frtb.marketdata.curve.CurveUnderlying;
import com.uob.frtb.ui.refdata.CUDefinitionWindowRefData;
import com.uob.frtb.ui.refdata.CurveDefWindowRefData;
import com.uob.frtb.ui.refdata.MarketDataSetWindowRefData;
import com.uob.frtb.ui.refdata.PricingContextConfigWindowRefData;
import com.uob.frtb.ui.refdata.PricingEnginesConfigWindowRefData;
import com.uob.frtb.ui.refdata.QuotesWindowRefData;
import com.uob.frtb.ui.refdata.SwapTradeWindowRefData;

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
