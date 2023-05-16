package com.uob.frtb.core.data.impl;

import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.core.data.CoreDataService;
import com.uob.frtb.core.exception.ApplicationException;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CoreDataServiceImpl implements CoreDataService {

	@Autowired
	private CoreDao coreDao;

	@Override
	@Transactional(readOnly = false)
	public <T> T save(T ser) {
		return coreDao.save(ser);
	}

	@Override
	@Transactional(readOnly = false)
	public <T> List<T> loadAll(Class<T> type) {
		return coreDao.loadAll(type);
	}

	@Override
	@Transactional(readOnly = false)
	public <T> List<T> save(List<T> list) {
		return coreDao.save(list);
	}

	@Override
	public <T> List<T> get(DetachedCriteria criteria) {
		return coreDao.get(criteria);
	}

	@Override
	public <T> T getUniqueResult(DetachedCriteria criteria) throws ApplicationException {
		return coreDao.getUniqueResult(criteria);
	}

	@Override
	@Transactional(readOnly = false)
	public <T> void delete(T obj) {
		coreDao.delete(obj);
	}

	@Override
	@Transactional(readOnly = false)
	public <T> void delete(List<T> list) {
		coreDao.delete(list);
	}

	@Override
	@Transactional(readOnly = false)
	public <T> T saveOrUpdate(T ser) {
		return coreDao.saveOrUpdate(ser);
	}

	@Override
	@Transactional(readOnly = false)
	public <T> List<T> saveOrUpdate(List<T> list) {
		return coreDao.saveOrUpdate(list);
	}
}
