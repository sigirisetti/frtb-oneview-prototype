package com.quark.db.schemadata.loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.quark.marketdata.curve.Curve;
import com.quark.marketdata.dao.MarketDataDao;
import com.quark.pricing.config.PricingContextConfig;
import com.quark.pricing.config.PricingEngineConfig;
import com.quark.pricing.config.PricingEngineConfigItem;
import com.quark.product.ProductType;

@Service
@Transactional
public class PricingEngineConfigSampleSetup {

	@Autowired
	private MarketDataDao dao;

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void setupSamplePricingEngines() {

		PricingEngineConfig peConfig = new PricingEngineConfig(PricingContextConfig.DEFAULT_PRICING_ENGINE_CONFIG);

		Curve usdCurve = dao.getCurve("USD", "USDLibor", "3M");
		PricingEngineConfigItem i1 = new PricingEngineConfigItem("USD_LIBOR",
				ProductType.VANILLA_SWAP.getProductType(), "USD", usdCurve);
		peConfig.addPricingEngineConfigItem(i1);

		Curve eurCurve = dao.getCurve("EUR", "Euribor", "6M");
		PricingEngineConfigItem i2 = new PricingEngineConfigItem("EUR_LIBOR",
				ProductType.VANILLA_SWAP.getProductType(), "EUR", eurCurve);
		peConfig.addPricingEngineConfigItem(i2);

		Curve gbpCurve = dao.getCurve("GBP", "GBPLibor", "6M");
		PricingEngineConfigItem i3 = new PricingEngineConfigItem("GBP_LIBOR",
				ProductType.VANILLA_SWAP.getProductType(), "GBP", gbpCurve);
		peConfig.addPricingEngineConfigItem(i3);

		dao.save(peConfig);
	}

}
