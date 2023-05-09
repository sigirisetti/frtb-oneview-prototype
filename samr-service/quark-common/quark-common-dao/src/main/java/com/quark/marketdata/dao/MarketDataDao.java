package com.quark.marketdata.dao;

import com.quark.marketdata.curve.Curve;
import com.quark.marketdata.curve.CurveUnderlying;
import com.quark.marketdata.curve.CurveUnderlyingMap;
import com.quark.marketdata.quote.Quote;
import com.quark.marketdata.quote.QuoteSet;
import com.quark.marketdata.search.QuoteSearchInput;
import com.quark.pricing.config.MarketDataSet;
import com.quark.pricing.config.PricingContextConfig;
import com.quark.pricing.config.PricingEngineConfig;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MarketDataDao {

	// Quotes
	List<String> getAllQuoteSetNames();

	Map<String, List<String>> getAllQuoteNames();

	void save(QuoteSet qs);

	void saveQuotes(List<Quote> quotes);

	void saveQuotes(String pCtxName, List<Quote> quotes);

	List<Quote> searchQuotes(QuoteSearchInput input);

	List<Quote> getQuotes(Date date, String pCtx, List<Quote> qList);

	QuoteSet getQuoteSet(String qsName);

	QuoteSet getQuoteSet(Timestamp ts, String qsName);

	List<Quote> loadQuotes(Timestamp ts, String qsName);

	List<Quote> loadAllQuotesForGivenDate(java.sql.Date date, String qsName);

	// Curve Underlying
	CurveUnderlying save(CurveUnderlying cu);

	void saveCurveUnderlyings(List<CurveUnderlying> list);

	void delete(CurveUnderlying... cu);

	List<CurveUnderlying> getCurveUnderlyings(Class type);

	List<CurveUnderlying> getAllCurveUnderlyings();

	// Curve
	List<Curve> getAllCurves();

	Curve save(Curve curve);

	Curve getCurve(Long pk);

	Curve getCurve(String currency, String rateIndex, String rateIndexTenor);

	Boolean deleteCurve(Long id);

	// Pricing Engine Config
	PricingEngineConfig save(PricingEngineConfig config);

	List<String> getPricingEngineConfigNames();

	PricingEngineConfig getPricingEngineConfig(String peConfig);

	Boolean delete(PricingEngineConfig peConfig);

	// Pricing Context Config
	List<String> getPricingCtxNames();

	String getPricingCtxQuoteSetName(String pCtxName);

	PricingContextConfig getPricingContextConfig(Timestamp ts, String name);

	PricingContextConfig getPricingContextConfig(String name);

	List<PricingContextConfig> getPricingContextConfigs();

	PricingContextConfig save(PricingContextConfig pc);

	Boolean delete(PricingContextConfig pc);

	List<CurveUnderlying> getCurveUnderlyings(List<CurveUnderlyingMap> underlyingMap);

	List<CurveUnderlying> getCurveUnderlyings(Collection<Long> underlyingIds);

	MarketDataSet save(MarketDataSet config);
}
