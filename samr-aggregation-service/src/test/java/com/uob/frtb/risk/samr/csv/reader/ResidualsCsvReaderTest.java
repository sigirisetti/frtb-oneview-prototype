package com.uob.frtb.risk.samr.csv.reader;

import org.junit.Test;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResidualsCsvReaderTest {

	@Test
    public void testDRCNonSecReader() throws IOException {
        String path = "../data/csv/samr/uploads/Residuals.csv";

		ResidualsCsvReader reader = new ResidualsCsvReader("Residuals", null);
		reader.setupCsvReader();
		reader.read(path);
		for (Object d : reader.getSamrData()) {
			log.info(d.toString());
		}
	}
}
