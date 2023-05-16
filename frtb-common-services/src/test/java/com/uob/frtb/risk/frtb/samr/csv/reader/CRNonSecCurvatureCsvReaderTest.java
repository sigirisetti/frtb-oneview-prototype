package com.uob.frtb.risk.frtb.samr.csv.reader;

import com.uob.frtb.risk.frtb.samr.csv.model.CRNonSecCurvatureData;

import org.junit.Test;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CRNonSecCurvatureCsvReaderTest {

	@Test
    public void testCRNonSecCurvatureCsvReader() throws IOException {
        String path = "../data/csv/samr/uploads/CRNonSecCurvature.csv";
		CRNonSecCurvatureCsvReader reader = new CRNonSecCurvatureCsvReader("CRNonSecCurvature", null);
		reader.setupCsvReader();
		reader.read(path);
		for (Object d : reader.getSamrData()) {
			log.info(((CRNonSecCurvatureData)d).buildTradeData().toString());
		}
	}
}
