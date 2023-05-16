package com.uob.frtb.risk.frtb.samr.csv.reader;

import com.fasterxml.jackson.databind.MappingIterator;
import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.risk.common.csv.reader.BaseCsvReader;
import com.uob.frtb.risk.common.model.BaseRiskEntity;
import com.uob.frtb.risk.frtb.samr.csv.model.SAMRData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all csv reader classes
 * 
 * @author Surya
 */
public abstract class BaseSAMRCsvReader<T extends BaseRiskEntity> extends BaseCsvReader {

	protected List<SAMRData<T>> samrData = new ArrayList<>();
	protected List<T> tradeData = new ArrayList<>();


	public BaseSAMRCsvReader(String csvName, CoreDao coreDao) {
		this.csvName = csvName;
		this.coreDao = coreDao;
	}

	public void read(String path) throws IOException {
		MappingIterator<SAMRData<T>> it = getReader().readValues(new File(path));
		while (it.hasNext()) {
			SAMRData<T> d = it.next();
			samrData.add(d);
			List<T> sens = d.buildTradeData();
			if (sens != null) {
				tradeData.addAll(sens);
			}
		}
		for (T sens : tradeData) {
			if (sens != null) {
				sens.setWorkflowInstance(workflowInstance);
			}
		}
	}

	public void write() {
		if (tradeData != null) {
			coreDao.save(tradeData);
		}
	}

	public List<SAMRData<T>> getSamrData() {
		return samrData;
	}
}
