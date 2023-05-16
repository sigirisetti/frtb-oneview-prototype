package com.uob.frtb.risk.samr.csv.reader;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.risk.samr.csv.model.IRVegaData;
import com.uob.frtb.risk.samr.model.TradeSensitivity;

public class IRVegaCsvReader extends BaseSAMRCsvReader<TradeSensitivity> {

	public IRVegaCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	public void setupCsvReader() {
		CsvSchema schema = CsvSchema.builder().setColumnSeparator(',').addColumn("tradeIdentifier")
				.addColumn("currency").addColumn("_6M_6M").addColumn("_6M_1Y").addColumn("_6M_3Y").addColumn("_6M_5Y")
				.addColumn("_6M_10Y").addColumn("_1Y_6M").addColumn("_1Y_1Y").addColumn("_1Y_3Y").addColumn("_1Y_5Y")
				.addColumn("_1Y_10Y").addColumn("_3Y_6M").addColumn("_3Y_1Y").addColumn("_3Y_3Y").addColumn("_3Y_5Y")
				.addColumn("_3Y_10Y").addColumn("_5Y_6M").addColumn("_5Y_1Y").addColumn("_5Y_3Y").addColumn("_5Y_5Y")
				.addColumn("_5Y_10Y").addColumn("_10Y_6M").addColumn("_10Y_1Y").addColumn("_10Y_3Y")
				.addColumn("_10Y_5Y").addColumn("_10Y_10Y").build().withSkipFirstDataRow(true);
		CsvMapper mapper = new CsvMapper();
		reader = mapper.readerFor(IRVegaData.class).with(schema);
	}
}
