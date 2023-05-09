package com.quark.risk.frtb.samr.csv.reader;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.quark.common.dao.CoreDao;
import com.quark.risk.frtb.samr.csv.model.EQVegaData;

public class EquityVegaCsvReader extends VegaCsvReader {

	public EquityVegaCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	public void setupCsvReader() {
		CsvSchema schema = CsvSchema.builder().setColumnSeparator(',').addColumn("tradeIdentifier")
				.addColumn("currency").addColumn("_6M").addColumn("_1Y").addColumn("_3Y").addColumn("_5Y")
				.addColumn("_10Y").build().withSkipFirstDataRow(true);
		CsvMapper mapper = new CsvMapper();
		reader = mapper.readerFor(EQVegaData.class).with(schema);
	}
}
