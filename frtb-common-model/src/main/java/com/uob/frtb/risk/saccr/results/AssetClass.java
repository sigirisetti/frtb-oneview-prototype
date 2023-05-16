package com.uob.frtb.risk.saccr.results;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetClass implements Serializable {

	private static final long serialVersionUID = -2391588954422704332L;

	private List<Currency> currencies;
}
