package com.quark.ui.refdata.service;

import com.quark.marketdata.curve.CurveUnderlying;
import com.quark.marketdata.dao.MarketDataDao;
import com.quark.marketdata.dao.ReferenceDataDao;
import com.quark.pricing.config.PricingContextConfig;
import com.quark.product.ProductType;
import com.quark.ui.refdata.CUDefinitionWindowRefData;
import com.quark.ui.refdata.CurveDefWindowRefData;
import com.quark.ui.refdata.MarketDataSetWindowRefData;
import com.quark.ui.refdata.PricingContextConfigWindowRefData;
import com.quark.ui.refdata.PricingEnginesConfigWindowRefData;
import com.quark.ui.refdata.QuotesWindowRefData;
import com.quark.ui.refdata.SwapTradeWindowRefData;
import com.quark.ui.refdata.WindowRefData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.TreeSet;

import static com.quark.refdata.model.ReferenceTypes.REF_TYPE_CALENDAR;
import static com.quark.refdata.model.ReferenceTypes.REF_TYPE_DATE_CONVENTION;
import static com.quark.refdata.model.ReferenceTypes.REF_TYPE_DATE_GEN_RULE;
import static com.quark.refdata.model.ReferenceTypes.REF_TYPE_DAY_COUNTER;
import static com.quark.refdata.model.ReferenceTypes.REF_TYPE_IBOR_INDEX;

@Transactional(readOnly = true)
@Service("UIRefDataServiceImpl")
public class UIRefDataServiceImpl implements UIRefDataService<CurveUnderlying> {

	@Autowired
	private ReferenceDataDao referenceDataDao;

	@Autowired
	private MarketDataDao marketDataDao;

	@Override
    public SwapTradeWindowRefData getSwapTradeWindowRefData() {
		SwapTradeWindowRefData d = new SwapTradeWindowRefData();
		d.setBusinessCalendars(referenceDataDao.getNamesAsList(REF_TYPE_CALENDAR));
		d.setIborIndexes(referenceDataDao.getNamesAsList(REF_TYPE_IBOR_INDEX));
		d.setDayCounters(referenceDataDao.getNamesAsList(REF_TYPE_DAY_COUNTER));
		d.setEffectiveDateconventions(referenceDataDao.getNamesAsList(REF_TYPE_DATE_CONVENTION));
		d.setPaymentBDCs(referenceDataDao.getNamesAsList(REF_TYPE_DATE_CONVENTION));
		d.setTerminationDateConventions(referenceDataDao.getNamesAsList(REF_TYPE_DATE_CONVENTION));
		d.setDateGenerationRule(referenceDataDao.getNamesAsList(REF_TYPE_DATE_GEN_RULE));
		return d;
	}

	@Override
    public QuotesWindowRefData getQuotesWindowRefData() {
		QuotesWindowRefData data = new QuotesWindowRefData();
		data.setQuoteSetNames(marketDataDao.getAllQuoteSetNames());
		data.setQuoteNames(marketDataDao.getAllQuoteNames());
		return data;
	}

	@Override
	public CurveDefWindowRefData getCurveDefWindowRefData(Set<String> refTypes) {
		CurveDefWindowRefData refData = new CurveDefWindowRefData();
		populateRefTypesAndGlobalProperties(refTypes, refData);
		refData.setPricingCtxNames(marketDataDao.getPricingCtxNames());
		refData.setCurveUnderlyingDefs(marketDataDao.getAllCurveUnderlyings());
		return refData;
	}

	private void populateRefTypesAndGlobalProperties(Set<String> refTypes, WindowRefData refData) {
		for (String rt : refTypes) {
			refData.addRefType(rt, referenceDataDao.getRefType(rt));
		}
	}

	@Override
	public CUDefinitionWindowRefData<CurveUnderlying> getCUDefinitionWindowRefData(Set<String> refTypes,
			Class<CurveUnderlying> typ) {
		CUDefinitionWindowRefData<CurveUnderlying> refData = new CUDefinitionWindowRefData<CurveUnderlying>();
		populateRefTypesAndGlobalProperties(refTypes, refData);
		refData.setUnderlyings(marketDataDao.getCurveUnderlyings(typ));
		return refData;
	}

	@Override
	public PricingEnginesConfigWindowRefData getPricingEngineConfigRefData(Set<String> refTypes) {
		PricingEnginesConfigWindowRefData data = new PricingEnginesConfigWindowRefData();
		populateRefTypesAndGlobalProperties(refTypes, data);
		data.setCurves(marketDataDao.getAllCurves());
		data.setPricingEngineConfigNames(marketDataDao.getPricingEngineConfigNames());
		return data;
	}

	@Override
	public PricingContextConfigWindowRefData getPricingContextRefData(Set<String> refTypes) {
		PricingContextConfigWindowRefData data = new PricingContextConfigWindowRefData();
		populateRefTypesAndGlobalProperties(refTypes, data);
		data.setPricingEngineConfigNames(new TreeSet<String>(marketDataDao.getPricingEngineConfigNames()));
		data.setQuoteSetNames(new TreeSet<String>(marketDataDao.getAllQuoteSetNames()));
		data.setPricingContext(new TreeSet<PricingContextConfig>(marketDataDao.getPricingContextConfigs()));
		return data;
	}

	@Override
	public MarketDataSetWindowRefData getMarketDataSetWindowRefData(Set<String> refTypes) {
		MarketDataSetWindowRefData data = new MarketDataSetWindowRefData();
		populateRefTypesAndGlobalProperties(refTypes, data);
		data.setProductTypes(ProductType.getProductTypeList());
		data.setCurves(marketDataDao.getAllCurves());
		return data;
	}
}
