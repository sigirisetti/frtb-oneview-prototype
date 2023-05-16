package com.uob.frtb.risk.saccr.csv.reader;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.risk.saccr.csv.model.CollateralData;
import com.uob.frtb.risk.saccr.model.Collateral;

public class CollateralCsvReader extends BaseSACCRCsvReader<Collateral> {

	public CollateralCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	@Override
	public void setupCsvReader() {
		CsvSchema schema = CsvSchema.builder().setColumnSeparator(',').addColumn("key").addColumn("counterparty")
				.addColumn("laId").addColumn("csaId").addColumn("collateralBalance").addColumn("haircut")
				.addColumn("thresholdForPo").addColumn("thresholdForCounterparty").addColumn("mtaForPo")
				.addColumn("mtaForCounterparty").addColumn("iaForPo").addColumn("iaForCounterparty").addColumn("nica")
				.addColumn("collateralCallFrequency").addColumn("dispute").addColumn("liquid").addColumn("clearing")
				.build().withSkipFirstDataRow(true);
		CsvMapper mapper = new CsvMapper();
		reader = mapper.readerFor(CollateralData.class).with(schema);
	}
}
