package com.quark.risk.frtb.samr.csv.reader;

import org.junit.Test;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CRNonSecDeltaCsvReaderTest {

	@Test
    public void testCRNonSecDeltaCsvReader() throws IOException {
        String path = "../../../data/csv/samr/uploads/CRNonSecDelta.csv";
		CRNonSecDeltaCsvReader reader = new CRNonSecDeltaCsvReader("CRNonSecDelta", null);
		reader.setupCsvReader();
		reader.read(path);
		for (Object d : reader.getSamrData()) {
			log.info(d.toString());
		}
	}
}
