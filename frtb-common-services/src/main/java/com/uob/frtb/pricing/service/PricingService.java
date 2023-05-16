package com.uob.frtb.pricing.service;

import com.uob.frtb.trade.SwapTrade;

public interface PricingService {

    double priceSwap(SwapTrade trade);
}
