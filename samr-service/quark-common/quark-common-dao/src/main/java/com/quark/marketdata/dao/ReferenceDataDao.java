package com.quark.marketdata.dao;

import com.quark.refdata.model.NameValuePair;

import java.util.List;

public interface ReferenceDataDao {

	public List<NameValuePair> getAllRefTypes();

	void save(String refType, List<NameValuePair> l);

	void delete(String refType);

	void delete(List<String> refTypes);

	List<String> getNamesAsList(String refType);

	List<NameValuePair> getRefType(String refType);
}
