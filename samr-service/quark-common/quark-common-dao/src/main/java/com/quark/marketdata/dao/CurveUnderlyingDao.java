package com.quark.marketdata.dao;

import com.quark.marketdata.curve.CurveUnderlyingMoneyMarket;
import com.quark.marketdata.curve.CurveUnderlyingSwap;

import java.util.List;

public interface CurveUnderlyingDao {
    void save(CurveUnderlyingMoneyMarket cu);

    List<CurveUnderlyingMoneyMarket> listCurveUnderlyingMoneyMarket();

    void save(CurveUnderlyingSwap curveUnderlyingSwap);

    List<CurveUnderlyingSwap> listCurveUnderlyingSwap();
}
