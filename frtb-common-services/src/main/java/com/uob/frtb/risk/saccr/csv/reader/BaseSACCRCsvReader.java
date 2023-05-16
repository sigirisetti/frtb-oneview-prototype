package com.uob.frtb.risk.saccr.csv.reader;

import com.fasterxml.jackson.databind.MappingIterator;
import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.risk.common.csv.reader.BaseCsvReader;
import com.uob.frtb.risk.common.model.BaseRiskEntity;
import com.uob.frtb.risk.saccr.csv.model.SACCRData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class BaseSACCRCsvReader<T extends BaseRiskEntity> extends BaseCsvReader {

	protected List<SACCRData<T>> saccrData = new ArrayList<>();
	protected List<T> saccrEntities = new ArrayList<>();

	public BaseSACCRCsvReader(String csvName, CoreDao coreDao) {
		this.csvName = csvName;
		this.coreDao = coreDao;
	}

    public void read(String path) throws IOException {
		MappingIterator<SACCRData<T>> it = getReader().readValues(new File(path));
		while (it.hasNext()) {
			SACCRData<T> d = it.next();
			if (d != null) {
				saccrData.add(d);
				if (d.getMessages() != null && !d.getMessages().isEmpty()) {
					log.info(d.getMessages().toString());
				}
				T e = d.build();
				if (e != null) {
					e.setWorkflowInstance(workflowInstance);
					saccrEntities.add(e);
				}
			}
		}
	}

	public void write() {
		if (saccrEntities != null) {
			coreDao.save(saccrEntities);
		}
	}
}
