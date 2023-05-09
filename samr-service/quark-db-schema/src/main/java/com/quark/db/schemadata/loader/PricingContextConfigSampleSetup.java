package com.quark.db.schemadata.loader;

import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.quark.marketdata.dao.MarketDataDao;
import com.quark.pricing.config.PricingContextConfig;

@Service
@Transactional
public class PricingContextConfigSampleSetup {


	@Autowired
	private MarketDataDao dao;

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void setupSamplePricingContext() {

		PricingContextConfig pcConfig = new PricingContextConfig(PricingContextConfig.DEFAULT_PRICING_ENGINE_CONFIG);
		pcConfig.setTimeZone(TimeZone.getTimeZone("GMT"));
		pcConfig.setCurrency("USD");
		pcConfig.setQuoteSet(dao.getQuoteSet(PricingContextConfig.DEFAULT_QUOTE_SET_NAME));
		pcConfig.setPricingEngineConfig(dao.getPricingEngineConfig(PricingContextConfig.DEFAULT_PRICING_ENGINE_CONFIG));
		dao.save(pcConfig);
	}
}
