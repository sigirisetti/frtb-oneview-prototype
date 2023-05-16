package com.uob.frtb.ui.refdata;

import com.uob.frtb.marketdata.curve.Curve;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarketDataSetWindowRefData extends WindowRefData implements Serializable {

	private static final long serialVersionUID = 4862201823706260673L;

	private List<String> productTypes;
	private List<Curve> curves;
}
