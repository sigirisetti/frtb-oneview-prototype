package com.quark.ui.refdata;

import com.quark.refdata.model.NameValuePair;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WindowRefData implements Serializable {

	private static final long serialVersionUID = 6627802961234699156L;

	private Map<String, List<NameValuePair>> refTypes = new HashMap<String, List<NameValuePair>>();

	public List<NameValuePair> getRefType(String refType) {
		return refTypes.get(refType);
	}

	public List<NameValuePair> addRefType(String refType, List<NameValuePair> list) {
		return refTypes.put(refType, list);
	}
}
