package com.uob.frtb.core.data;

import com.uob.frtb.core.exception.ApplicationException;

import org.hibernate.criterion.DetachedCriteria;

import java.util.List;

public interface CoreDataService {
	<T> T save(T ser);

	<T> T saveOrUpdate(T ser);

	<T> List<T> loadAll(Class<T> type);

	<T> List<T> save(List<T> list);

	<T> List<T> saveOrUpdate(List<T> list);

	<T> List<T> get(DetachedCriteria criteria);

	<T> T getUniqueResult(DetachedCriteria criteria) throws ApplicationException;

	<T> void delete(T obj);

	<T> void delete(List<T> list);
}
