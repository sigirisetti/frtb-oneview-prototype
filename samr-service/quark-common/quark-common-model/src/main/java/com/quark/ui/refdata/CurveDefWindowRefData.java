package com.quark.ui.refdata;

import com.quark.marketdata.curve.CurveUnderlying;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurveDefWindowRefData extends WindowRefData implements Serializable {

	private static final long serialVersionUID = 1239456358981604924L;

	private List<CurveUnderlying> curveUnderlyingDefs;
	private List<String> pricingCtxNames;
}
