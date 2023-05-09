package com.quark.marketdata.dao.impl;

import com.quark.marketdata.curve.CurveUnderlyingMoneyMarket;
import com.quark.marketdata.curve.CurveUnderlyingSwap;
import com.quark.marketdata.dao.CurveUnderlyingDao;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class CurveUnderlyingDaoImplTest {

    //@Test
	public void test() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("quark-dao.xml");

		CurveUnderlyingDao cuDao = context.getBean(CurveUnderlyingDao.class);

		cuDao.save(new CurveUnderlyingMoneyMarket("USDLibor", "USD", "Close", "1D", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		cuDao.save(new CurveUnderlyingMoneyMarket("USDLibor", "USD", "Close", "1W", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		cuDao.save(new CurveUnderlyingMoneyMarket("USDLibor", "USD", "Close", "1M", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		cuDao.save(new CurveUnderlyingMoneyMarket("USDLibor", "USD", "Close", "3M", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		cuDao.save(new CurveUnderlyingMoneyMarket("USDLibor", "USD", "Close", "6M", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		cuDao.save(new CurveUnderlyingMoneyMarket("USDLibor", "USD", "Close", "9M", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));
		cuDao.save(new CurveUnderlyingMoneyMarket("USDLibor", "USD", "Close", "1Y", "TARGET", "ModifiedFollowing",
				"Actual360", 2, true, "Reuters"));

		List<CurveUnderlyingMoneyMarket> list = cuDao.listCurveUnderlyingMoneyMarket();

		for (CurveUnderlyingMoneyMarket u : list) {
			System.out.println("CurveUnderlying List::" + u);
		}

		cuDao.save(new CurveUnderlyingSwap("USD", "Close", "2Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "USDLibor", "Reuters"));
		cuDao.save(new CurveUnderlyingSwap("USD", "Close", "3Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "USDLibor", "Reuters"));
		cuDao.save(new CurveUnderlyingSwap("USD", "Close", "4Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "USDLibor", "Reuters"));
		cuDao.save(new CurveUnderlyingSwap("USD", "Close", "5Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "USDLibor", "Reuters"));
		cuDao.save(new CurveUnderlyingSwap("USD", "Close", "6Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "USDLibor", "Reuters"));
		cuDao.save(new CurveUnderlyingSwap("USD", "Close", "7Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "USDLibor", "Reuters"));
		cuDao.save(new CurveUnderlyingSwap("USD", "Close", "8Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "USDLibor", "Reuters"));
		cuDao.save(new CurveUnderlyingSwap("USD", "Close", "9Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "USDLibor", "Reuters"));
		cuDao.save(new CurveUnderlyingSwap("USD", "Close", "10Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "USDLibor", "Reuters"));
		cuDao.save(new CurveUnderlyingSwap("USD", "Close", "15Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "USDLibor", "Reuters"));
		cuDao.save(new CurveUnderlyingSwap("USD", "Close", "20Y", "TARGET", "ModifiedFollowing", "Actual360",
				"Semiannual", "Quarterly", "USDLibor", "Reuters"));

		List<CurveUnderlyingSwap> list2 = cuDao.listCurveUnderlyingSwap();

		for (CurveUnderlyingSwap u : list2) {
			System.out.println("CurveUnderlying List::" + u);
		}

		context.close();
	}

}
