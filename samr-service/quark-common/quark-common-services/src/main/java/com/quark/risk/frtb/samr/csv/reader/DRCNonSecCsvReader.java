package com.quark.risk.frtb.samr.csv.reader;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.quark.common.dao.CoreDao;
import com.quark.risk.frtb.samr.csv.model.DRCNonSecData;
import com.quark.risk.frtb.samr.model.DRCNonSec;

public class DRCNonSecCsvReader extends BaseSAMRCsvReader<DRCNonSec> {

	public DRCNonSecCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	public void setupCsvReader() {
		CsvSchema schema = CsvSchema.builder().setColumnSeparator(',').addColumn("tradeIdentifier")
				.addColumn("currency").addColumn("issuerId").addColumn("issuer").addColumn("bucket")
				.addColumn("product").addColumn("seniority").addColumn("rating").addColumn("longOrShort")
				.addColumn("maturity").addColumn("notional").addColumn("mtm").addColumn("strike").build()
				.withSkipFirstDataRow(true);
		CsvMapper mapper = new CsvMapper();
		reader = mapper.readerFor(DRCNonSecData.class).with(schema);
	}
}
