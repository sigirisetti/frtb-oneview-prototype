package com.uob.frtb.ui.refdata;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuotesWindowRefData implements Serializable {

	private static final long serialVersionUID = -3974505678636251269L;
	private List<String> quoteSetNames;
	private Map<String, List<String>> quoteNames;
}
