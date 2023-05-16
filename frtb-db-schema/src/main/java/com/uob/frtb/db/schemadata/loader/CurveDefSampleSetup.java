package com.uob.frtb.db.schemadata.loader;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.uob.frtb.marketdata.curve.Curve;
import com.uob.frtb.marketdata.curve.CurveUnderlying;
import com.uob.frtb.marketdata.curve.CurveUnderlyingMap;
import com.uob.frtb.marketdata.curve.CurveUnderlyingType;
import com.uob.frtb.pricing.config.PricingContextConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.uob.frtb.marketdata.dao.MarketDataDao;

@Service
@Transactional
public class CurveDefSampleSetup {

	@Autowired
	private MarketDataDao dao;

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void setupSampleCurveDefs() {
		List<CurveUnderlying> all = dao.getAllCurveUnderlyings();
		setupSampleUSDCurve(all);
		setupSampleEURCurve(all);
		setupSampleGBPCurve(all);
	}

	private void setupSampleEURCurve(List<CurveUnderlying> all) {
		Curve curve = new Curve();
		curve.setCalendar("TARGET");
		curve.setCurrency("EUR");
		curve.setInstance("CLOSE");
		curve.setInterpolator("LogLinearInterpolation");
		curve.setName("EUR_EURIBOR_6M");
		curve.setPricingContext(PricingContextConfig.DEFAULT_PRICING_CONTEXT_CONFIG);
		curve.setRateIndex("Euribor");
		curve.setRateIndexTenor("6M");
		curve.setDayCounter("Actual360");
		curve.setTimestamp(new Timestamp(System.currentTimeMillis()));
		List<CurveUnderlyingMap> cuList = getUnderlyings(all, "EUR", new CurveUnderlyingType[] {
				CurveUnderlyingType.MONEY_MARKET, CurveUnderlyingType.SWAP });
		curve.setUnderlyingMap(cuList);
		dao.save(curve);
	}

	private void setupSampleUSDCurve(List<CurveUnderlying> all) {
		Curve curve = new Curve();
		curve.setCalendar("TARGET");
		curve.setCurrency("USD");
		curve.setInstance("CLOSE");
		curve.setInterpolator("LogLinearInterpolation");
		curve.setName("USD_LIBOR_3M");
		curve.setPricingContext(PricingContextConfig.DEFAULT_PRICING_CONTEXT_CONFIG);
		curve.setRateIndex("USDLibor");
		curve.setRateIndexTenor("3M");
		curve.setDayCounter("Actual360");
		curve.setTimestamp(new Timestamp(System.currentTimeMillis()));
		List<CurveUnderlyingMap> cuList = getUnderlyings(all, "USD", new CurveUnderlyingType[] {
				CurveUnderlyingType.MONEY_MARKET, CurveUnderlyingType.SWAP });
		curve.setUnderlyingMap(cuList);
		dao.save(curve);
	}

	private void setupSampleGBPCurve(List<CurveUnderlying> all) {
		Curve curve = new Curve();
		curve.setCalendar("TARGET");
		curve.setCurrency("GBP");
		curve.setInstance("CLOSE");
		curve.setInterpolator("LogLinearInterpolation");
		curve.setName("GBP_LIBOR_6M");
		curve.setPricingContext(PricingContextConfig.DEFAULT_PRICING_CONTEXT_CONFIG);
		curve.setRateIndex("GBPLibor");
		curve.setRateIndexTenor("6M");
		curve.setDayCounter("Actual360");
		curve.setTimestamp(new Timestamp(System.currentTimeMillis()));
		List<CurveUnderlyingMap> cuList = getUnderlyings(all, "GBP", new CurveUnderlyingType[] {
				CurveUnderlyingType.MONEY_MARKET, CurveUnderlyingType.SWAP });
		curve.setUnderlyingMap(cuList);
		dao.save(curve);
	}

	private List<CurveUnderlyingMap> getUnderlyings(List<CurveUnderlying> all, String ccy,
			CurveUnderlyingType[] curveUnderlyingTypes) {
		List<CurveUnderlyingMap> cuList = new ArrayList<CurveUnderlyingMap>();
		for (CurveUnderlyingType cut : curveUnderlyingTypes) {
			for (CurveUnderlying cu : all) {
				if (cut.type().equals(cu.getType()) && ccy.equals(cu.getCurrency())) {
					cuList.add(new CurveUnderlyingMap(null, cu.getId()));
				}
			}
		}
		return cuList;
	}
}
