package com.uob.frtb.pricing.service;

import com.uob.frtb.trade.SwapTrade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service("QLPricingService")
public class QLPricingService implements PricingService {

	@Override
	public double priceSwap(SwapTrade trade) {

		return 100;
	}

}
