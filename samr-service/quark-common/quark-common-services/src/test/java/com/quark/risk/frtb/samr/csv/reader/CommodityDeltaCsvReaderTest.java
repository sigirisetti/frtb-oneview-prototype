package com.quark.risk.frtb.samr.csv.reader;

import org.junit.Test;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommodityDeltaCsvReaderTest {

	@Test
    public void testCreditIssuerInfoCsvReader() throws IOException {
        String path = "../../../data/csv/samr/uploads/COMMDelta.csv";

		CommodityDeltaCsvReader reader = new CommodityDeltaCsvReader("COMMDelta", null);
		reader.setupCsvReader();
		reader.read(path);
		for (Object d : reader.getSamrData()) {
			log.info(d.toString());
		}
	}

}
