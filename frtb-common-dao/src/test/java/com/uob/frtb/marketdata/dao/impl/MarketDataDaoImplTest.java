package com.uob.frtb.marketdata.dao.impl;

import com.uob.frtb.marketdata.dao.MarketDataDao;
import com.uob.frtb.marketdata.quote.Quote;
import com.uob.frtb.marketdata.quote.QuoteName;
import com.uob.frtb.marketdata.quote.QuoteSet;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class MarketDataDaoImplTest {

    //@Test
	public void test() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("frtb-common-dao.xml");
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
