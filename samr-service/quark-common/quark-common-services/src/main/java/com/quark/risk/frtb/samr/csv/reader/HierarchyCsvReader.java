package com.quark.risk.frtb.samr.csv.reader;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.quark.common.dao.CoreDao;
import com.quark.risk.frtb.samr.csv.model.HierarchyData;
import com.quark.risk.frtb.samr.model.Hierarchy;

public class HierarchyCsvReader extends BaseSAMRCsvReader<Hierarchy> {

	public HierarchyCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	@Override
	public void setupCsvReader() {
		CsvSchema schema = CsvSchema.builder().setColumnSeparator(',').addColumn("tradeIdentifier").addColumn("po")
				.addColumn("product").addColumn("desk").addColumn("book").addColumn("location").build()
				.withSkipFirstDataRow(true);
		CsvMapper mapper = new CsvMapper();
		reader = mapper.readerFor(HierarchyData.class).with(schema);
	}
}
