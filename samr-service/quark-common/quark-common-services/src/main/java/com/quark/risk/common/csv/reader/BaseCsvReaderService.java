package com.quark.risk.common.csv.reader;

import com.quark.core.exception.ApplicationException;
import com.quark.risk.common.model.WorkflowInstance;
import com.quark.risk.common.model.WorkflowInstanceProperties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public abstract class BaseCsvReaderService {

	@Transactional
	public void loadData(WorkflowInstance wfInst) throws ApplicationException, IOException {
		String dir = getUploadDir(wfInst);

		Map<String, CsvReader> readers = init();
		for (Map.Entry<String, CsvReader> me : readers.entrySet()) {
			log.info("Reading CSV : {} using reader : {}", me.getKey(), me.getValue().getClass().getName());
			String path = dir.concat(me.getKey().concat(".csv"));
			File f = new File(path);
			if (!f.exists()) {
				log.info("{} file doesn't exists. Checked path {}", me.getKey(), path);
				continue;
			}
			me.getValue().setupCsvReader();
			me.getValue().setWorkflowInstance(wfInst);
			me.getValue().read(path);
			me.getValue().write();
		}
	}

	protected abstract Map<String, CsvReader> init();

	protected String getUploadDir(WorkflowInstance wfInst) throws ApplicationException {
		String dir = "";
		for (WorkflowInstanceProperties p : wfInst.getProperties()) {
			if ("uploadDir".equals(p.getName())) {
				dir = p.getValue();
			}
		}
		if (StringUtils.isBlank(dir)) {
			throw new ApplicationException(-1, "uploadDir property is not set");
		}
		if (!dir.endsWith("/")) {
			dir = dir.concat("/");
		}
		return dir;
	}
}
