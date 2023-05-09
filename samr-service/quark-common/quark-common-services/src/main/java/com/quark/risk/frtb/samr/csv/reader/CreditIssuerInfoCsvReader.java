package com.quark.risk.frtb.samr.csv.reader;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.quark.common.dao.CoreDao;
import com.quark.risk.frtb.samr.csv.model.CreditIssuerData;
import com.quark.risk.frtb.samr.model.CreditIssuerInfo;

public class CreditIssuerInfoCsvReader extends BaseSAMRCsvReader<CreditIssuerInfo> {

	public CreditIssuerInfoCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	public void setupCsvReader() {
		CsvSchema schema = CsvSchema.builder().setColumnSeparator(',').addColumn("tradeIdentifier")
				.addColumn("currency").addColumn("bucket").addColumn("issuer").addColumn("bondOrCds")
				.addColumn("rating").addColumn("sector").build().withSkipFirstDataRow(true);
		CsvMapper mapper = new CsvMapper();
		reader = mapper.readerFor(CreditIssuerData.class).with(schema);
	}
}
