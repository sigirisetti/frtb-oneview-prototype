package com.quark.risk.common.csv.reader;

import com.fasterxml.jackson.databind.ObjectReader;
import com.quark.common.dao.CoreDao;
import com.quark.risk.common.model.WorkflowInstance;

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
