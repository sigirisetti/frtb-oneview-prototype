package com.uob.frtb.db.schemadata.loader;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.uob.frtb.marketdata.quote.Quote;
import com.uob.frtb.marketdata.quote.QuoteName;
import com.uob.frtb.marketdata.quote.QuoteSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.uob.frtb.marketdata.dao.MarketDataDao;

@Service
@Transactional
public class QuoteDataSetup {

	@Autowired
	private MarketDataDao dao;

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void setupQuotes() {
		QuoteSet qs = new QuoteSet("default", "default");

		Set<Quote> qList = new HashSet<Quote>();

		qList.add(new Quote(new QuoteName("MoneyMarket.USD.USDLibor.1D/Reuters", "Yield"), new Timestamp(System
				.currentTimeMillis()), qs, 0.0056, 0.0056, 0.0056, 0.0056));
		qList.add(new Quote(new QuoteName("MoneyMarket.USD.USDLibor.1W/Reuters", "Yield"), new Timestamp(System
				.currentTimeMillis()), qs, 0.0066, 0.0066, 0.0066, 0.0066));
		qList.add(new Quote(new QuoteName("MoneyMarket.USD.USDLibor.1M/Reuters", "Yield"), new Timestamp(System
				.currentTimeMillis()), qs, 0.0078, 0.0078, 0.0078, 0.0078));
		qList.add(new Quote(new QuoteName("MoneyMarket.USD.USDLibor.3M/Reuters", "Yield"), new Timestamp(System
				.currentTimeMillis()), qs, 0.00888, 0.00888, 0.00888, 0.00888));
		qList.add(new Quote(new QuoteName("MoneyMarket.USD.USDLibor.6M/Reuters", "Yield"), new Timestamp(System
				.currentTimeMillis()), qs, 0.00961, 0.00961, 0.00961, 0.00961));
		qList.add(new Quote(new QuoteName("MoneyMarket.USD.USDLibor.9M/Reuters", "Yield"), new Timestamp(System
				.currentTimeMillis()), qs, 0.0098, 0.0098, 0.0098, 0.0098));
		qList.add(new Quote(new QuoteName("MoneyMarket.USD.USDLibor.1Y/Reuters", "Yield"), new Timestamp(System
				.currentTimeMillis()), qs, 0.0105, 0.0105, 0.0105, 0.0105));

		qList.add(new Quote(new QuoteName("MoneyMarket.EUR.Euribor.1D/Reuters", "Yield"), new Timestamp(System
				.currentTimeMillis()), qs, 0.0056, 0.0056, 0.0056, 0.0056));
		qList.add(new Quote(new QuoteName("MoneyMarket.EUR.Euribor.1W/Reuters", "Yield"), new Timestamp(System
				.currentTimeMillis()), qs, 0.0066, 0.0066, 0.0066, 0.0066));
		qList.add(new Quote(new QuoteName("MoneyMarket.EUR.Euribor.1M/Reuters", "Yield"), new Timestamp(System
				.currentTimeMillis()), qs, 0.0078, 0.0078, 0.0078, 0.0078));
		qList.add(new Quote(new QuoteName("MoneyMarket.EUR.Euribor.3M/Reuters", "Yield"), new Timestamp(System
				.currentTimeMillis()), qs, 0.00888, 0.00888, 0.00888, 0.00888));
		qList.add(new Quote(new QuoteName("MoneyMarket.EUR.Euribor.6M/Reuters", "Yield"), new Timestamp(System
				.currentTimeMillis()), qs, 0.00961, 0.00961, 0.00961, 0.00961));
		qList.add(new Quote(new QuoteName("MoneyMarket.EUR.Euribor.9M/Reuters", "Yield"), new Timestamp(System
				.currentTimeMillis()), qs, 0.0098, 0.0098, 0.0098, 0.0098));
		qList.add(new Quote(new QuoteName("MoneyMarket.EUR.Euribor.1Y/Reuters", "Yield"), new Timestamp(System
				.currentTimeMillis()), qs, 0.0105, 0.0105, 0.0105, 0.0105));

		qList.add(new Quote(new QuoteName("MoneyMarket.GBP.GBPLibor.1D/Reuters", "Yield"), new Timestamp(System
				.currentTimeMillis()), qs, 0.0056, 0.0056, 0.0056, 0.0056));
		qList.add(new Quote(new QuoteName("MoneyMarket.GBP.GBPLibor.1W/Reuters", "Yield"), new Timestamp(System
				.currentTimeMillis()), qs, 0.0066, 0.0066, 0.0066, 0.0066));
		qList.add(new Quote(new QuoteName("MoneyMarket.GBP.GBPLibor.1M/Reuters", "Yield"), new Timestamp(System
				.currentTimeMillis()), qs, 0.0078, 0.0078, 0.0078, 0.0078));
		qList.add(new Quote(new QuoteName("MoneyMarket.GBP.GBPLibor.3M/Reuters", "Yield"), new Timestamp(System
				.currentTimeMillis()), qs, 0.00888, 0.00888, 0.00888, 0.00888));
		qList.add(new Quote(new QuoteName("MoneyMarket.GBP.GBPLibor.6M/Reuters", "Yield"), new Timestamp(System
				.currentTimeMillis()), qs, 0.00961, 0.00961, 0.00961, 0.00961));
		qList.add(new Quote(new QuoteName("MoneyMarket.GBP.GBPLibor.9M/Reuters", "Yield"), new Timestamp(System
				.currentTimeMillis()), qs, 0.0098, 0.0098, 0.0098, 0.0098));
		qList.add(new Quote(new QuoteName("MoneyMarket.GBP.GBPLibor.1Y/Reuters", "Yield"), new Timestamp(System
				.currentTimeMillis()), qs, 0.0105, 0.0105, 0.0105, 0.0105));

		qList.add(new Quote(new QuoteName("Swap.2Y.USD/Semiannual/USDLibor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.0132, 0.0132, 0.0132, 0.0132));
		qList.add(new Quote(new QuoteName("Swap.3Y.USD/Semiannual/USDLibor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.0178, 0.0178, 0.0178, 0.0178));
		qList.add(new Quote(new QuoteName("Swap.4Y.USD/Semiannual/USDLibor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.0199, 0.0199, 0.0199, 0.0199));
		qList.add(new Quote(new QuoteName("Swap.5Y.USD/Semiannual/USDLibor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.025, 0.025, 0.025, 0.025));
		qList.add(new Quote(new QuoteName("Swap.6Y.USD/Semiannual/USDLibor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.0333, 0.0333, 0.0333, 0.0333));
		qList.add(new Quote(new QuoteName("Swap.7Y.USD/Semiannual/USDLibor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.038, 0.038, 0.038, 0.038));
		qList.add(new Quote(new QuoteName("Swap.8Y.USD/Semiannual/USDLibor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.041, 0.041, 0.041, 0.041));
		qList.add(new Quote(new QuoteName("Swap.9Y.USD/Semiannual/USDLibor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.046, 0.046, 0.046, 0.046));
		qList.add(new Quote(new QuoteName("Swap.10Y.USD/Semiannual/USDLibor.Quarterly/Reuters", "Yield"),
				new Timestamp(System.currentTimeMillis()), qs, 0.048, 0.048, 0.048, 0.048));
		qList.add(new Quote(new QuoteName("Swap.15Y.USD/Semiannual/USDLibor.Quarterly/Reuters", "Yield"),
				new Timestamp(System.currentTimeMillis()), qs, 0.05, 0.05, 0.05, 0.05));
		qList.add(new Quote(new QuoteName("Swap.20Y.USD/Semiannual/USDLibor.Quarterly/Reuters", "Yield"),
				new Timestamp(System.currentTimeMillis()), qs, 0.065, 0.065, 0.065, 0.065));

		qList.add(new Quote(new QuoteName("Swap.2Y.EUR/Semiannual/Euribor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.0132, 0.0132, 0.0132, 0.0132));
		qList.add(new Quote(new QuoteName("Swap.3Y.EUR/Semiannual/Euribor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.0178, 0.0178, 0.0178, 0.0178));
		qList.add(new Quote(new QuoteName("Swap.4Y.EUR/Semiannual/Euribor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.0199, 0.0199, 0.0199, 0.0199));
		qList.add(new Quote(new QuoteName("Swap.5Y.EUR/Semiannual/Euribor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.025, 0.025, 0.025, 0.025));
		qList.add(new Quote(new QuoteName("Swap.6Y.EUR/Semiannual/Euribor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.0333, 0.0333, 0.0333, 0.0333));
		qList.add(new Quote(new QuoteName("Swap.7Y.EUR/Semiannual/Euribor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.038, 0.038, 0.038, 0.038));
		qList.add(new Quote(new QuoteName("Swap.8Y.EUR/Semiannual/Euribor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.041, 0.041, 0.041, 0.041));
		qList.add(new Quote(new QuoteName("Swap.9Y.EUR/Semiannual/Euribor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.046, 0.046, 0.046, 0.046));
		qList.add(new Quote(new QuoteName("Swap.10Y.EUR/Semiannual/Euribor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.048, 0.048, 0.048, 0.048));
		qList.add(new Quote(new QuoteName("Swap.15Y.EUR/Semiannual/Euribor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.05, 0.05, 0.05, 0.05));
		qList.add(new Quote(new QuoteName("Swap.20Y.EUR/Semiannual/Euribor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.065, 0.065, 0.065, 0.065));

		qList.add(new Quote(new QuoteName("Swap.2Y.GBP/Semiannual/GBPLibor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.0132, 0.0132, 0.0132, 0.0132));
		qList.add(new Quote(new QuoteName("Swap.3Y.GBP/Semiannual/GBPLibor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.0178, 0.0178, 0.0178, 0.0178));
		qList.add(new Quote(new QuoteName("Swap.4Y.GBP/Semiannual/GBPLibor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.0199, 0.0199, 0.0199, 0.0199));
		qList.add(new Quote(new QuoteName("Swap.5Y.GBP/Semiannual/GBPLibor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.025, 0.025, 0.025, 0.025));
		qList.add(new Quote(new QuoteName("Swap.6Y.GBP/Semiannual/GBPLibor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.0333, 0.0333, 0.0333, 0.0333));
		qList.add(new Quote(new QuoteName("Swap.7Y.GBP/Semiannual/GBPLibor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.038, 0.038, 0.038, 0.038));
		qList.add(new Quote(new QuoteName("Swap.8Y.GBP/Semiannual/GBPLibor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.041, 0.041, 0.041, 0.041));
		qList.add(new Quote(new QuoteName("Swap.9Y.GBP/Semiannual/GBPLibor.Quarterly/Reuters", "Yield"), new Timestamp(
				System.currentTimeMillis()), qs, 0.046, 0.046, 0.046, 0.046));
		qList.add(new Quote(new QuoteName("Swap.10Y.GBP/Semiannual/GBPLibor.Quarterly/Reuters", "Yield"),
				new Timestamp(System.currentTimeMillis()), qs, 0.048, 0.048, 0.048, 0.048));
		qList.add(new Quote(new QuoteName("Swap.15Y.GBP/Semiannual/GBPLibor.Quarterly/Reuters", "Yield"),
				new Timestamp(System.currentTimeMillis()), qs, 0.05, 0.05, 0.05, 0.05));
		qList.add(new Quote(new QuoteName("Swap.20Y.GBP/Semiannual/GBPLibor.Quarterly/Reuters", "Yield"),
				new Timestamp(System.currentTimeMillis()), qs, 0.065, 0.065, 0.065, 0.065));

		qs.setQuotes(qList);
		dao.save(qs);

	}
}
