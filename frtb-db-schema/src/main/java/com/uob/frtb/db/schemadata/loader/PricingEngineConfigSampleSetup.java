package com.uob.frtb.db.schemadata.loader;

import com.uob.frtb.marketdata.curve.Curve;
import com.uob.frtb.pricing.config.PricingContextConfig;
import com.uob.frtb.pricing.config.PricingEngineConfig;
import com.uob.frtb.pricing.config.PricingEngineConfigItem;
import com.uob.frtb.product.ProductType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.uob.frtb.marketdata.dao.MarketDataDao;

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
