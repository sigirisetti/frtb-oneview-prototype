package com.uob.frtb.ui.refdata;

import com.uob.frtb.marketdata.curve.Curve;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PricingEnginesConfigWindowRefData extends WindowRefData implements Serializable {

	private static final long serialVersionUID = -77568507944602345L;

	private List<Curve> curves;
	private List<String> pricingEngineConfigNames;
}
