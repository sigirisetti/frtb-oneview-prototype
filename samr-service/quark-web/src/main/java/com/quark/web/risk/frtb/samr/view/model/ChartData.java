package com.quark.web.risk.frtb.samr.view.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChartData {

	private String key;
	private List<KeyValue> values = new ArrayList<>();

	public ChartData(String key) {
		this.key = key;
	}

	public void addBar(KeyValue b) {
		values.add(b);
	}
}
