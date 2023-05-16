package com.uob.frtb.risk.common.csv.reader;

import com.fasterxml.jackson.databind.ObjectReader;
import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.risk.common.model.WorkflowInstance;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseCsvReader implements CsvReader {
	protected String csvName;
	protected CoreDao coreDao;
	protected WorkflowInstance workflowInstance;
	protected ObjectReader reader;
}
