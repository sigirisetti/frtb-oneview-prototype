package com.uob.frtb.risk.samr.csv.reader;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.risk.samr.csv.model.DRCSecCTPData;
import com.uob.frtb.risk.samr.model.DRCSecCTP;

public class DRCSecCTPCsvReader extends BaseSAMRCsvReader<DRCSecCTP> {
	public DRCSecCTPCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	public void setupCsvReader() {
		CsvSchema schema = CsvSchema.builder().setColumnSeparator(',').addColumn("tradeIdentifier")
				.addColumn("currency").addColumn("index").addColumn("series").addColumn("bucket").addColumn("product")
				.addColumn("securitization").addColumn("rating").addColumn("region").addColumn("longOrShort")
				.addColumn("maturity").addColumn("notional").addColumn("mtm").build().withSkipFirstDataRow(true);
		CsvMapper mapper = new CsvMapper();
		reader = mapper.readerFor(DRCSecCTPData.class).with(schema);
	}
}
