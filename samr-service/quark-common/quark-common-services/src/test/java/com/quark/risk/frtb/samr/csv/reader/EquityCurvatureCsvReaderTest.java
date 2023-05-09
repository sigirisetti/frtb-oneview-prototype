package com.quark.risk.frtb.samr.csv.reader;

import org.junit.Test;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EquityCurvatureCsvReaderTest {

	@Test
    public void testEquityCurvatureCsvReader() throws IOException {
        String path = "../../data/csv/samr/uploads/EqCurvature.csv";

		EquityCurvatureCsvReader reader = new EquityCurvatureCsvReader("EqCurvature", null);
		reader.setupCsvReader();
		reader.read(path);
		for (Object d : reader.getSamrData()) {
			log.info(d.toString());
		}
	}
}
