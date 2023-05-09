package com.quark.risk.saccr.results;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Currency implements Serializable {

	private static final long serialVersionUID = -6827694238276602487L;

	private List<NettingSet> nettingSets;

}
