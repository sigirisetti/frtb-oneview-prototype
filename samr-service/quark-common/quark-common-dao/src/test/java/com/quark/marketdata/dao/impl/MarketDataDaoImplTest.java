package com.quark.marketdata.dao.impl;

import com.quark.marketdata.dao.MarketDataDao;
import com.quark.marketdata.quote.Quote;
import com.quark.marketdata.quote.QuoteName;
import com.quark.marketdata.quote.QuoteSet;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class MarketDataDaoImplTest {

    //@Test
	public void test() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("quark-dao.xml");
		QuoteName qn1 = new QuoteName("FX.EUR.USD", "");
		QuoteSet qs = new QuoteSet();
		qs.setQuoteSetName("SOD");
		qs.setDescription("Start of Day");
		Set<Quote> quotes = new HashSet<Quote>();
		Quote q1 = new Quote(qn1, new Timestamp(System.currentTimeMillis()), qs, 1.212, 1.223, 1.222, 1.211);
		q1.setQuoteName(qn1);
		q1.setQuoteSet(qs);
		quotes.add(q1);
		qs.setQuotes(quotes);

		MarketDataDao mdDao = context.getBean(MarketDataDao.class);
		mdDao.save(qs);
		context.close();
	}

}
