package com.uob.frtb.risk.saccr.csv.reader;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.risk.saccr.csv.model.MarketData;
import com.uob.frtb.risk.saccr.model.SaccrMarketQuote;

public class QuotesCsvReader extends BaseSACCRCsvReader<SaccrMarketQuote> {

	public QuotesCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	@Override
	public void setupCsvReader() {
		CsvSchema schema = CsvSchema.builder().setColumnSeparator(',').addColumn("quoteName").addColumn("quoteValue")
				.build().withSkipFirstDataRow(true);
		CsvMapper mapper = new CsvMapper();
		reader = mapper.readerFor(MarketData.class).with(schema);
	}
}
