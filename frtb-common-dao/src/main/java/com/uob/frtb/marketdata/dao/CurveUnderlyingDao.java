package com.uob.frtb.marketdata.dao;

import com.uob.frtb.marketdata.curve.CurveUnderlyingMoneyMarket;
import com.uob.frtb.marketdata.curve.CurveUnderlyingSwap;

import java.util.List;

public interface CurveUnderlyingDao {
    void save(CurveUnderlyingMoneyMarket cu);

    List<CurveUnderlyingMoneyMarket> listCurveUnderlyingMoneyMarket();

    void save(CurveUnderlyingSwap curveUnderlyingSwap);

    List<CurveUnderlyingSwap> listCurveUnderlyingSwap();
}
