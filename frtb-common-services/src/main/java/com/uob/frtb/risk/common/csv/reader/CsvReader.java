package com.uob.frtb.risk.common.csv.reader;

import com.uob.frtb.risk.common.model.WorkflowInstance;

import java.io.IOException;

public interface CsvReader {
	void setupCsvReader();

	void setWorkflowInstance(WorkflowInstance wfInst);

	void read(String path) throws IOException;

	void write();
}
