package com.quark.refdata.service;

import com.quark.marketdata.dao.ReferenceDataDao;
import com.quark.refdata.model.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service("ReferenceDataServiceImpl")
public class ReferenceDataServiceImpl implements ReferenceDataService {

	@Autowired
	private ReferenceDataDao referenceDataDao;

	@Override
	public List<NameValuePair> getAllRefTypes() {
		return referenceDataDao.getAllRefTypes();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void save(String refType, List<NameValuePair> l) {
		referenceDataDao.save(refType, l);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void delete(String refType) {
		referenceDataDao.delete(refType);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void delete(List<String> refTypes) {
		referenceDataDao.delete(refTypes);
	}
}
