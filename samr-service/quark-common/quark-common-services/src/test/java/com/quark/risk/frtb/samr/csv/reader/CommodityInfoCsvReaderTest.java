package com.quark.risk.frtb.samr.csv.reader;

import org.junit.Test;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommodityInfoCsvReaderTest {

	@Test
    public void testCreditIssuerInfoCsvReader() throws IOException {
		String path = "../../data/csv/samr/uploads/CommodityInfo.csv";
		CommodityInfoCsvReader reader = new CommodityInfoCsvReader("CommodityInfo", null);
		reader.setupCsvReader();
		reader.read(path);
		for (Object d : reader.getSamrData()) {
			log.info(d.toString());
		}
	}

}
