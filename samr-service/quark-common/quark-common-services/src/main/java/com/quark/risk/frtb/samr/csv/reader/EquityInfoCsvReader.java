package com.quark.risk.frtb.samr.csv.reader;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.quark.common.dao.CoreDao;
import com.quark.risk.frtb.samr.csv.model.EQInfoData;
import com.quark.risk.frtb.samr.model.EquityInfo;

public class EquityInfoCsvReader extends BaseSAMRCsvReader<EquityInfo> {

	public EquityInfoCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	@Override
	public void setupCsvReader() {
		CsvSchema schema = CsvSchema.builder().setColumnSeparator(',').addColumn("tradeIdentifier")
				.addColumn("currency").addColumn("uniqueIndexKey").addColumn("bucket").addColumn("product")
				.addColumn("issuer").addColumn("repo").addColumn("marketCap").addColumn("economy").addColumn("sector")
				.build().withSkipFirstDataRow(true);
		CsvMapper mapper = new CsvMapper();
		reader = mapper.readerFor(EQInfoData.class).with(schema);
	}
}
