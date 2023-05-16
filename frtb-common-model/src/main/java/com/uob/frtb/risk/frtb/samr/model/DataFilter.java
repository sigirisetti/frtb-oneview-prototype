package com.uob.frtb.risk.frtb.samr.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DataFilter {
	private String po;
	private String filterName;
	private String filterValue;
}
