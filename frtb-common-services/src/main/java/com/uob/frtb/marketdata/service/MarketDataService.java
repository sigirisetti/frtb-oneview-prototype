package com.uob.frtb.marketdata.service;

import com.uob.frtb.marketdata.curve.Curve;
import com.uob.frtb.marketdata.curve.CurveUnderlying;
import com.uob.frtb.marketdata.quote.Quote;
import com.uob.frtb.marketdata.search.QuoteSearchInput;
import com.uob.frtb.pricing.config.MarketDataSet;
import com.uob.frtb.pricing.config.PricingContextConfig;
import com.uob.frtb.pricing.config.PricingEngineConfig;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public interface MarketDataService {

	// Quotes
	List<String> getAllQuoteSetNames();

	List<Quote> searchQuotes(QuoteSearchInput input);

	void saveQuotes(String string, List<Quote> quotes);

	void saveQuotes(List<Quote> quotes);

	List<Quote> getQuotes(Date date, String pCtx, List<Quote> qList);

	List<Quote> loadAllQuotesForGivenDate(java.sql.Date date, String qsName);

	// Curve Underlyings
	CurveUnderlying save(CurveUnderlying cu);

	void delete(CurveUnderlying... cu);

	void saveCurveUnderlyings(List<CurveUnderlying> list);

	// Curve
	Curve save(Curve curve);

	Curve getCurve(Long long1);

	Curve getCurve(String currency, String rateIndex, String rateIndexTenor);

	List<Curve> getAllCurves();

	Boolean deleteCurve(Long id);

	// Pricing Engine config
	PricingEngineConfig save(PricingEngineConfig config);

	PricingEngineConfig getPricingEngineConfig(String peConfig);

	Boolean delete(PricingEngineConfig current);

	// Pricing context config
	PricingContextConfig save(PricingContextConfig pc);

	PricingContextConfig getPricingContextConfig(Timestamp ts, String name);

	PricingContextConfig getPricingContextConfig(String name);

	List<PricingContextConfig> getPricingContextConfigs();

	Boolean delete(PricingContextConfig pc);

	PricingContextConfig getDefaultPricingContextConfig();

	MarketDataSet save(MarketDataSet config);
}
