package com.uob.frtb.risk.frtb.samr.csv.reader;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.risk.frtb.samr.csv.model.DRCSecNonCTPData;
import com.uob.frtb.risk.frtb.samr.model.DRCSecNonCTP;

public class DRCSecNonCTPCsvReader extends BaseSAMRCsvReader<DRCSecNonCTP> {

	public DRCSecNonCTPCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	public void setupCsvReader() {
		CsvSchema schema = CsvSchema.builder().setColumnSeparator(',').addColumn("tradeIdentifier")
				.addColumn("currency").addColumn("portfolio").addColumn("bucket").addColumn("product")
				.addColumn("bondOrCds").addColumn("rating").addColumn("category").addColumn("region")
				.addColumn("longOrShort").addColumn("seniority").addColumn("maturity").addColumn("notional")
				.addColumn("mtm").build().withSkipFirstDataRow(true);
		CsvMapper mapper = new CsvMapper();
		reader = mapper.readerFor(DRCSecNonCTPData.class).with(schema);
	}
}
