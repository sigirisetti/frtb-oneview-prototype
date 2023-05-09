package com.quark.refdata.service;

import com.quark.refdata.model.NameValuePair;

import java.util.List;

public interface ReferenceDataService {

	public List<NameValuePair> getAllRefTypes();

	public void save(String refType, List<NameValuePair> l);

	void delete(String refType);

	void delete(List<String> refTypes);
}
