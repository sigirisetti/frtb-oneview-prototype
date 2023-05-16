package com.uob.frtb.marketdata.service;

import com.uob.frtb.marketdata.curve.Curve;
import com.uob.frtb.marketdata.curve.CurveUnderlying;
import com.uob.frtb.marketdata.dao.MarketDataDao;
import com.uob.frtb.marketdata.quote.Quote;
import com.uob.frtb.marketdata.search.QuoteSearchInput;
import com.uob.frtb.pricing.config.MarketDataSet;
import com.uob.frtb.pricing.config.PricingContextConfig;
import com.uob.frtb.pricing.config.PricingEngineConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Transactional(readOnly = true)
@Service("MarketDataServiceImpl")
public class MarketDataServiceImpl implements MarketDataService {

	@Autowired
	private MarketDataDao dao;

	@Override
	public List<String> getAllQuoteSetNames() {
		return dao.getAllQuoteSetNames();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void saveQuotes(String pCtxName, List<Quote> quotes) {
		dao.saveQuotes(pCtxName, quotes);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void saveQuotes(List<Quote> quotes) {
		dao.saveQuotes(quotes);
	}

	@Override
	public List<Quote> searchQuotes(QuoteSearchInput input) {
		return dao.searchQuotes(input);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public CurveUnderlying save(CurveUnderlying cu) {
		return dao.save(cu);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void delete(CurveUnderlying... cu) {
		dao.delete(cu);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void saveCurveUnderlyings(List<CurveUnderlying> list) {
		dao.saveCurveUnderlyings(list);
	}

	@Override
	public PricingContextConfig getDefaultPricingContextConfig() {
		PricingContextConfig pCtx = new PricingContextConfig();
		return pCtx;
	}

	@Override
	public List<Quote> getQuotes(Date date, String pCtx, List<Quote> qList) {
		return dao.getQuotes(date, pCtx, qList);
	}

	@Override
	public List<Quote> loadAllQuotesForGivenDate(java.sql.Date date, String qsName) {
		return dao.loadAllQuotesForGivenDate(date, qsName);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Curve save(Curve curve) {
		return dao.save(curve);
	}

	@Override
	public Curve getCurve(Long pk) {
		Curve curve = dao.getCurve(pk);
		// curve.setQuotes(dao.getQuotes());
		return curve;
	}

	@Override
	public List<Curve> getAllCurves() {
		return dao.getAllCurves();
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Boolean deleteCurve(Long id) {
		return dao.deleteCurve(id);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public PricingEngineConfig save(PricingEngineConfig config) {
		return dao.save(config);
	}

	@Override
	public PricingEngineConfig getPricingEngineConfig(String peConfig) {
		return dao.getPricingEngineConfig(peConfig);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Boolean delete(PricingEngineConfig peConfig) {
		return dao.delete(peConfig);
	}

	@Override
	public Curve getCurve(String currency, String rateIndex, String rateIndexTenor) {
		return dao.getCurve(currency, rateIndex, rateIndexTenor);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public PricingContextConfig save(PricingContextConfig pc) {
		return dao.save(pc);
	}

	@Override
	public PricingContextConfig getPricingContextConfig(Timestamp ts, String name) {
		return dao.getPricingContextConfig(ts, name);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Boolean delete(PricingContextConfig pc) {
		return dao.delete(pc);
	}

	@Override
	public PricingContextConfig getPricingContextConfig(String name) {
		return dao.getPricingContextConfig(name);
	}

	@Override
	public List<PricingContextConfig> getPricingContextConfigs() {
		return dao.getPricingContextConfigs();
	}

	@Override
	public MarketDataSet save(MarketDataSet config) {
		return dao.save(config);
	}
}
