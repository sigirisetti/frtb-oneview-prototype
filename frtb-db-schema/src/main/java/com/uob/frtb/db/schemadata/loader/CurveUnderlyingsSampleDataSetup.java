package com.uob.frtb.db.schemadata.loader;

import java.util.ArrayList;
import java.util.List;

import com.uob.frtb.marketdata.curve.CurveUnderlying;
import com.uob.frtb.marketdata.curve.CurveUnderlyingMoneyMarket;
import com.uob.frtb.marketdata.curve.CurveUnderlyingSwap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.uob.frtb.marketdata.dao.MarketDataDao;

@Service
@Transactional
public class CurveUnderlyingsSampleDataSetup {

	@Autowired
	private MarketDataDao dao;

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void setupSampleCurveUnderlyings() {
		setupSampleUSDMoneyMarketUnderlyings();
		setupSampleUSDSwapUnderlyings();
		setupSampleEURMoneyMarketUnderlyings();
		setupSampleEURSwapUnderlyings();
		setupSampleGBPMoneyMarketUnderlyings();
		setupSampleGBPSwapUnderlyings();
	}

	private void setupSampleUSDSwapUnderlyings() {
		List<CurveUnderlying> list = new ArrayList<CurveUnderlying>();
		list.add(new CurveUnderlyingSwap("USD", "Yield", "2Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "USDLibor", "Reuters"));
		list.add(new CurveUnderlyingSwap("USD", "Yield", "3Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "USDLibor", "Reuters"));
		list.add(new CurveUnderlyingSwap("USD", "Yield", "4Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "USDLibor", "Reuters"));
		list.add(new CurveUnderlyingSwap("USD", "Yield", "5Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "USDLibor", "Reuters"));
		list.add(new CurveUnderlyingSwap("USD", "Yield", "6Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "USDLibor", "Reuters"));
		list.add(new CurveUnderlyingSwap("USD", "Yield", "7Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "USDLibor", "Reuters"));
		list.add(new CurveUnderlyingSwap("USD", "Yield", "8Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "USDLibor", "Reuters"));
		list.add(new CurveUnderlyingSwap("USD", "Yield", "9Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "USDLibor", "Reuters"));
		list.add(new CurveUnderlyingSwap("USD", "Yield", "10Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "USDLibor", "Reuters"));
		list.add(new CurveUnderlyingSwap("USD", "Yield", "15Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "USDLibor", "Reuters"));
		list.add(new CurveUnderlyingSwap("USD", "Yield", "20Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "USDLibor", "Reuters"));

		dao.saveCurveUnderlyings(list);
	}

	private void setupSampleEURSwapUnderlyings() {
		List<CurveUnderlying> list = new ArrayList<CurveUnderlying>();
		list.add(new CurveUnderlyingSwap("EUR", "Yield", "2Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "Euribor", "Reuters"));
		list.add(new CurveUnderlyingSwap("EUR", "Yield", "3Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "Euribor", "Reuters"));
		list.add(new CurveUnderlyingSwap("EUR", "Yield", "4Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "Euribor", "Reuters"));
		list.add(new CurveUnderlyingSwap("EUR", "Yield", "5Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "Euribor", "Reuters"));
		list.add(new CurveUnderlyingSwap("EUR", "Yield", "6Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "Euribor", "Reuters"));
		list.add(new CurveUnderlyingSwap("EUR", "Yield", "7Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "Euribor", "Reuters"));
		list.add(new CurveUnderlyingSwap("EUR", "Yield", "8Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "Euribor", "Reuters"));
		list.add(new CurveUnderlyingSwap("EUR", "Yield", "9Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "Euribor", "Reuters"));
		list.add(new CurveUnderlyingSwap("EUR", "Yield", "10Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "Euribor", "Reuters"));
		list.add(new CurveUnderlyingSwap("EUR", "Yield", "15Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "Euribor", "Reuters"));
		list.add(new CurveUnderlyingSwap("EUR", "Yield", "20Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "Euribor", "Reuters"));

		dao.saveCurveUnderlyings(list);
	}

	private void setupSampleGBPSwapUnderlyings() {
		List<CurveUnderlying> list = new ArrayList<CurveUnderlying>();
		list.add(new CurveUnderlyingSwap("GBP", "Yield", "2Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "GBPLibor", "Reuters"));
		list.add(new CurveUnderlyingSwap("GBP", "Yield", "3Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "GBPLibor", "Reuters"));
		list.add(new CurveUnderlyingSwap("GBP", "Yield", "4Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "GBPLibor", "Reuters"));
		list.add(new CurveUnderlyingSwap("GBP", "Yield", "5Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "GBPLibor", "Reuters"));
		list.add(new CurveUnderlyingSwap("GBP", "Yield", "6Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "GBPLibor", "Reuters"));
		list.add(new CurveUnderlyingSwap("GBP", "Yield", "7Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "GBPLibor", "Reuters"));
		list.add(new CurveUnderlyingSwap("GBP", "Yield", "8Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "GBPLibor", "Reuters"));
		list.add(new CurveUnderlyingSwap("GBP", "Yield", "9Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "GBPLibor", "Reuters"));
		list.add(new CurveUnderlyingSwap("GBP", "Yield", "10Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "GBPLibor", "Reuters"));
		list.add(new CurveUnderlyingSwap("GBP", "Yield", "15Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "GBPLibor", "Reuters"));
		list.add(new CurveUnderlyingSwap("GBP", "Yield", "20Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "GBPLibor", "Reuters"));

		dao.saveCurveUnderlyings(list);
	}

	private void setupSampleUSDMoneyMarketUnderlyings() {
		List<CurveUnderlying> list = new ArrayList<CurveUnderlying>();
		list.add(new CurveUnderlyingMoneyMarket("USDLibor", "USD", "Yield", "1D", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		list.add(new CurveUnderlyingMoneyMarket("USDLibor", "USD", "Yield", "1W", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		list.add(new CurveUnderlyingMoneyMarket("USDLibor", "USD", "Yield", "1M", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		list.add(new CurveUnderlyingMoneyMarket("USDLibor", "USD", "Yield", "3M", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		list.add(new CurveUnderlyingMoneyMarket("USDLibor", "USD", "Yield", "6M", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		list.add(new CurveUnderlyingMoneyMarket("USDLibor", "USD", "Yield", "9M", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		list.add(new CurveUnderlyingMoneyMarket("USDLibor", "USD", "Yield", "1Y", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		dao.saveCurveUnderlyings(list);
	}

	private void setupSampleEURMoneyMarketUnderlyings() {
		List<CurveUnderlying> list = new ArrayList<CurveUnderlying>();
		list.add(new CurveUnderlyingMoneyMarket("Euribor", "EUR", "Yield", "1D", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		list.add(new CurveUnderlyingMoneyMarket("Euribor", "EUR", "Yield", "1W", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		list.add(new CurveUnderlyingMoneyMarket("Euribor", "EUR", "Yield", "1M", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		list.add(new CurveUnderlyingMoneyMarket("Euribor", "EUR", "Yield", "3M", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		list.add(new CurveUnderlyingMoneyMarket("Euribor", "EUR", "Yield", "6M", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		list.add(new CurveUnderlyingMoneyMarket("Euribor", "EUR", "Yield", "9M", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		list.add(new CurveUnderlyingMoneyMarket("Euribor", "EUR", "Yield", "1Y", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		dao.saveCurveUnderlyings(list);
	}

	private void setupSampleGBPMoneyMarketUnderlyings() {
		List<CurveUnderlying> list = new ArrayList<CurveUnderlying>();
		list.add(new CurveUnderlyingMoneyMarket("GBPLibor", "GBP", "Yield", "1D", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		list.add(new CurveUnderlyingMoneyMarket("GBPLibor", "GBP", "Yield", "1W", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		list.add(new CurveUnderlyingMoneyMarket("GBPLibor", "GBP", "Yield", "1M", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		list.add(new CurveUnderlyingMoneyMarket("GBPLibor", "GBP", "Yield", "3M", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		list.add(new CurveUnderlyingMoneyMarket("GBPLibor", "GBP", "Yield", "6M", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		list.add(new CurveUnderlyingMoneyMarket("GBPLibor", "GBP", "Yield", "9M", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		list.add(new CurveUnderlyingMoneyMarket("GBPLibor", "GBP", "Yield", "1Y", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		dao.saveCurveUnderlyings(list);
	}

}
