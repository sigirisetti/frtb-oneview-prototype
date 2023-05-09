package com.quark.risk.frtb.samr.csv.reader;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.quark.common.dao.CoreDao;
import com.quark.risk.frtb.samr.csv.model.EQDeltaData;
import com.quark.risk.frtb.samr.model.TradeSensitivity;

public class EquityDeltaCsvReader extends BaseSAMRCsvReader<TradeSensitivity> {

	public EquityDeltaCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	public void setupCsvReader() {
		CsvSchema schema = CsvSchema.builder().setColumnSeparator(',').addColumn("tradeIdentifier")
				.addColumn("currency").addColumn("repoIndicator").addColumn("value").build().withSkipFirstDataRow(true);
		CsvMapper mapper = new CsvMapper();
		reader = mapper.readerFor(EQDeltaData.class).with(schema);
	}
}
