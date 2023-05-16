package com.uob.frtb.risk.samr.csv.reader;

import org.junit.Test;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DRCSecNonCTPReaderTest {

	@Test
    public void testDRCSecNonCTPReader() throws IOException {
        String path = "../data/csv/samr/uploads/DRCSecNonCTP.csv";

		DRCSecNonCTPCsvReader reader = new DRCSecNonCTPCsvReader("DRCSecNonCTP", null);
		reader.setupCsvReader();
		reader.read(path);
		for (Object d : reader.getSamrData()) {
			log.info(d.toString());
		}
	}

}
