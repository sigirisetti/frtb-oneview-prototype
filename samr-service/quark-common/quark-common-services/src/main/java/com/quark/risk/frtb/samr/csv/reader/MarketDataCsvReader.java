package com.quark.risk.frtb.samr.csv.reader;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.quark.common.dao.CoreDao;
import com.quark.risk.frtb.samr.csv.model.MarketData;
import com.quark.risk.frtb.samr.model.SamrMarketQuote;

public class MarketDataCsvReader extends BaseSAMRCsvReader<SamrMarketQuote> {

	public MarketDataCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	public void setupCsvReader() {
		CsvSchema schema = CsvSchema.builder().setColumnSeparator(',').addColumn("quote").addColumn("value").build()
				.withSkipFirstDataRow(true);
		schema.withSkipFirstDataRow(true);
		CsvMapper mapper = new CsvMapper();
		reader = mapper.readerFor(MarketData.class).with(schema);
	}
}