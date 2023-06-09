package com.uob.frtb.risk.samr.csv.reader;

import org.junit.Test;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CRNonSecVegaCsvReaderTest {

	@Test
    public void testCRNonSecVegaCsvReader() throws IOException {
        String path = "../data/csv/samr/uploads/CRNonSecVega.csv";
		CRNonSecVegaCsvReader reader = new CRNonSecVegaCsvReader("CRNonSecVega", null);
		reader.setupCsvReader();
		reader.read(path);
		for (Object d : reader.getSamrData()) {
			log.info(d.toString());
		}
	}
}
