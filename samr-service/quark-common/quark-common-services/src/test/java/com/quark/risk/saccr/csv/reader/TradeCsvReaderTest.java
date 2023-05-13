package com.quark.risk.saccr.csv.reader;

import com.quark.common.dao.CoreDao;
import com.quark.core.service.test.BaseServiceTest;
import com.quark.risk.saccr.csv.model.TradeData;
import com.quark.risk.saccr.model.RequiredTradeAttributes;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TradeCsvReaderTest extends BaseServiceTest {


	@Autowired
	private CoreDao coreDao;

	@Test
	@Transactional
    public void testCollateralCsvReader() throws IOException {
		List<RequiredTradeAttributes> configList = coreDao.loadAll(RequiredTradeAttributes.class);
		Map<String, RequiredTradeAttributes> config = new HashMap<>();
		for (RequiredTradeAttributes r : configList) {
			config.put(r.getProductName(), r);
		}
		String path = "../../../data/csv/saccr/config/Trades.csv";
		TradeCsvReader reader = new TradeCsvReader("Trades", null);
		reader.setConfig(config);
		reader.setupCsvReader();
		reader.read(path);
		for (Object d : reader.getSaccrData()) {
			log.info(((TradeData) d).build().toString());
		}

		try (FileWriter fw = new FileWriter(path)) {
			for (Object d : reader.getSaccrData()) {
				TradeData td = (TradeData) d;
				fw.write(td.toCsv());
				log.info(td.build().toString());
				fw.write("\n");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}
}
