package com.uob.frtb.risk.samr.results;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntermediateResultsData {
	private List<String> headers;
	private List<Object> data;
}
