package com.uob.frtb.risk.samr.csv.reader;

import org.junit.Test;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DRCSecCTPReaderTest {

	@Test
    public void testDRCSecCTPReader() throws IOException {
        String path = "../data/csv/samr/uploads/DRCSecCTP.csv";

		DRCSecCTPCsvReader reader = new DRCSecCTPCsvReader("DRCSecCTP", null);
		reader.setupCsvReader();
		reader.read(path);
		for (Object d : reader.getSamrData()) {
			log.info(d.toString());
		}
	}

}
