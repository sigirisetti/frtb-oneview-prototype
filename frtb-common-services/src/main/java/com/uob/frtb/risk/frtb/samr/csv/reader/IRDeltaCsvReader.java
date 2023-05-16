package com.uob.frtb.risk.frtb.samr.csv.reader;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.risk.frtb.samr.csv.model.IRDeltaData;
import com.uob.frtb.risk.frtb.samr.model.TradeSensitivity;

public class IRDeltaCsvReader extends BaseSAMRCsvReader<TradeSensitivity> {

	public IRDeltaCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	public void setupCsvReader() {
		CsvSchema schema = CsvSchema.builder().setColumnSeparator(',').addColumn("tradeIdentifier")
				.addColumn("indexName").addColumn("currency").addColumn("_3M").addColumn("_6M").addColumn("_1Y")
				.addColumn("_2Y").addColumn("_3Y").addColumn("_5Y").addColumn("_7Y").addColumn("_10Y").addColumn("_15Y")
				.addColumn("_30Y").addColumn("inflation").addColumn("basis").build().withSkipFirstDataRow(true);
		CsvMapper mapper = new CsvMapper();
		reader = mapper.readerFor(IRDeltaData.class).with(schema);
	}
}
