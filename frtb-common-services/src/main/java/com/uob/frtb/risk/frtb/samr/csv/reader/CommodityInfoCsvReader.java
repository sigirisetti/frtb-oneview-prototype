package com.uob.frtb.risk.frtb.samr.csv.reader;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.risk.frtb.samr.csv.model.CommodityInfoData;
import com.uob.frtb.risk.frtb.samr.model.CommodityInfo;

public class CommodityInfoCsvReader extends BaseSAMRCsvReader<CommodityInfo> {

	public CommodityInfoCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	public void setupCsvReader() {
		CsvSchema schema = CsvSchema.builder().setColumnSeparator(',').addColumn("tradeIdentifier").addColumn("bucket")
				.addColumn("product").addColumn("underlying").addColumn("grade").addColumn("deliveryLocation").build()
				.withSkipFirstDataRow(true);
		CsvMapper mapper = new CsvMapper();
		reader = mapper.readerFor(CommodityInfoData.class).with(schema);
	}

}
