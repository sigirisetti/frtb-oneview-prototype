package com.uob.frtb.risk.saccr.csv.reader;

import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.core.exception.ApplicationException;
import com.uob.frtb.risk.common.csv.reader.BaseCsvReaderService;
import com.uob.frtb.risk.common.csv.reader.CsvReader;
import com.uob.frtb.risk.common.model.WorkflowInstance;
import com.uob.frtb.risk.saccr.csv.model.SACCRData;
import com.uob.frtb.risk.saccr.model.RequiredTradeAttributes;
import com.uob.frtb.risk.saccr.results.SACCRValidationMessages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SACCRCsvReaderService extends BaseCsvReaderService {

	@Value("${saccr.trade.filename:Trades}")
	private String tradeCsvFilename;

	@Value("${saccr.collateral.filename:Collateral}")
	private String collateralCsvFilename;

	@Value("${saccr.marketdata.filename:MarketData}")
	private String marketDataCsvFilename;

	@Autowired
	private CoreDao coreDao;

	protected Map<String, CsvReader> init() {
		Map<String, RequiredTradeAttributes> config = getRequiredTradeAttributes();
		Map<String, CsvReader> readers = new LinkedHashMap<>();
		TradeCsvReader tradeCsvReader = new TradeCsvReader(tradeCsvFilename, coreDao);
		tradeCsvReader.setConfig(config);
		readers.put(tradeCsvFilename, tradeCsvReader);
		readers.put(collateralCsvFilename, new CollateralCsvReader(collateralCsvFilename, coreDao));
		readers.put(marketDataCsvFilename, new QuotesCsvReader(marketDataCsvFilename, coreDao));
		return readers;
	}

	private Map<String, RequiredTradeAttributes> getRequiredTradeAttributes() {
		List<RequiredTradeAttributes> configList = coreDao.loadAll(RequiredTradeAttributes.class);
		Map<String, RequiredTradeAttributes> config = new HashMap<>();
		for (RequiredTradeAttributes r : configList) {
			config.put(r.getProductName(), r);
		}
		return config;
	}

	@Transactional
	public Map<String, List<SACCRData>> validate(WorkflowInstance wfInst) throws ApplicationException, IOException {
		String dir = getUploadDir(wfInst);
		Map<String, CsvReader> readers = init();
		Map<String, List<SACCRData>> failedValidation = new HashMap<>();
		for (Map.Entry<String, CsvReader> me : readers.entrySet()) {
			log.info("Reading SACCR {} csv using reader {}", me.getKey(), me.getValue().getClass().getName());
			if (!dir.endsWith("/")) {
				dir = dir.concat("/");
			}
			String path = dir.concat(me.getKey().concat(".csv"));
			File f = new File(path);
			if (!f.exists()) {
				log.info("{} sensitivity file is not uploaded. Checked path {}", me.getKey(), path);
				continue;
			}
			me.getValue().setupCsvReader();
			me.getValue().setWorkflowInstance(wfInst);
			me.getValue().read(path);
			List<SACCRData> data = ((BaseSACCRCsvReader) me.getValue()).getSaccrData();
			List<SACCRData> failed = new ArrayList<>();
			for (SACCRData d : data) {
				if (d.getMessages() != null) {
					failed.add(d);
				}
			}
			failedValidation.put(me.getKey(), failed);
		}
		saveValidationMessages(wfInst, failedValidation);
		return failedValidation;
	}

	private void saveValidationMessages(WorkflowInstance wfInst, Map<String, List<SACCRData>> failedValidation) {
		SACCRValidationMessages msgs = new SACCRValidationMessages();
		msgs.setValidationMessages(failedValidation);
		msgs.setWorkflowInstance(wfInst);
		coreDao.save(msgs);
	}
}
