package com.quark.risk.common.csv.reader;

import com.quark.risk.common.model.WorkflowInstance;

import java.io.IOException;

public interface CsvReader {
	void setupCsvReader();

	void setWorkflowInstance(WorkflowInstance wfInst);

	void read(String path) throws IOException;

	void write();
}
