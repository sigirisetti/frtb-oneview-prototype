package com.quark.risk.saccr.csv.reader;

import com.quark.risk.saccr.csv.model.CollateralData;
import com.quark.risk.saccr.model.Collateral;

import org.junit.Test;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CollateralCsvReaderTest {

    @Test
    public void testCollateralCsvReader() throws IOException {
        String path = "../../../data/csv/saccr/config/Collateral.csv";

        CollateralCsvReader reader = new CollateralCsvReader("Collateral", null);
        reader.setupCsvReader();
        reader.read(path);
        for (Object d : reader.getSaccrData()) {
            if (d != null) {
                Collateral c = ((CollateralData) d).build();
                if (c != null) {
                    log.info(c.toString());
                } else {
                    log.error("Failed to build collateral");
                }
            }
        }
    }
}
