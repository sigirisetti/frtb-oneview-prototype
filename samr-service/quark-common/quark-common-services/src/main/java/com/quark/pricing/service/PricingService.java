package com.quark.pricing.service;

import com.quark.trade.SwapTrade;

public interface PricingService {

    double priceSwap(SwapTrade trade);
}
