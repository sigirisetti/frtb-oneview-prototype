package com.quark.risk.frtb.samr.csv.reader;

import org.junit.Test;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DRCNonSecReaderTest {

	@Test
    public void testDRCNonSecReader() throws IOException {
        String path = "../../../data/csv/samr/uploads/DRCNonSec.csv";

		DRCNonSecCsvReader reader = new DRCNonSecCsvReader("DRCNonSec", null);
		reader.setupCsvReader();
		reader.read(path);
		for (Object d : reader.getSamrData()) {
			log.info(d.toString());
		}
	}
}
