package com.uob.frtb.risk.frtb.samr.csv.reader;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.risk.frtb.samr.csv.model.CommodityDeltaData;
import com.uob.frtb.risk.frtb.samr.model.TradeSensitivity;

public class CommodityDeltaCsvReader extends BaseSAMRCsvReader<TradeSensitivity> {

	public CommodityDeltaCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	public void setupCsvReader() {
		CsvSchema schema = CsvSchema.builder().setColumnSeparator(',').addColumn("tradeIdentifier")
				.addColumn("currency").addColumn("spot").addColumn("_3M").addColumn("_6M").addColumn("_1Y")
				.addColumn("_2Y").addColumn("_3Y").addColumn("_5Y").addColumn("_10Y").addColumn("_15Y")
				.addColumn("_20Y").addColumn("_30Y").build().withSkipFirstDataRow(true);
		CsvMapper mapper = new CsvMapper();
		reader = mapper.readerFor(CommodityDeltaData.class).with(schema);
	}

}
