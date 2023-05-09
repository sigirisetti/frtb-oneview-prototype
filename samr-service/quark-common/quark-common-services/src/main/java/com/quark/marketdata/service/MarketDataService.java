package com.quark.marketdata.service;

import com.quark.marketdata.curve.Curve;
import com.quark.marketdata.curve.CurveUnderlying;
import com.quark.marketdata.quote.Quote;
import com.quark.marketdata.search.QuoteSearchInput;
import com.quark.pricing.config.MarketDataSet;
import com.quark.pricing.config.PricingContextConfig;
import com.quark.pricing.config.PricingEngineConfig;

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
