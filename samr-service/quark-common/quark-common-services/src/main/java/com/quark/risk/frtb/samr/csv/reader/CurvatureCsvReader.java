package com.quark.risk.frtb.samr.csv.reader;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.quark.common.dao.CoreDao;
import com.quark.risk.common.model.BaseRiskEntity;
import com.quark.risk.frtb.samr.csv.model.SAMRData;

public abstract class CurvatureCsvReader<D extends BaseRiskEntity, T extends SAMRData<D>> extends BaseSAMRCsvReader<D> {

	public CurvatureCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	public void setupCsvReader() {
		CsvSchema schema = CsvSchema.builder().setColumnSeparator(',').addColumn("tradeIdentifier")
				.addColumn("currency").addColumn("mtmBase").addColumn("mtmUpShock").addColumn("mtmDownShock")
				.addColumn("deltaShock").build().withSkipFirstDataRow(true);
		CsvMapper mapper = new CsvMapper();
		reader = mapper.readerFor(getCsvReaderType()).with(schema);
	}

	public abstract Class<T> getCsvReaderType();
}
