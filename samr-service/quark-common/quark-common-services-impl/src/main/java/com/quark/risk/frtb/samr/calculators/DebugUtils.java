package com.quark.risk.frtb.samr.calculators;

import com.quark.risk.frtb.samr.calculators.EQCurvatureRiskChargeCalc.EqCurvatureData;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DebugUtils {

	@Value("${frtb.samr.debug.csv:true}")
	private boolean writeCsv;

	@Value("${frtb.samr.debug.csv.dir:c:/tmp/debug/csv/}")
	private String csvdir;

	public void writeToCsv(Map<String, EqCurvatureData> aggrByIssuer) {
		if (!writeCsv) {
			return;
		}
		mkdirs(csvdir);
		String f = csvdir + "EqCurvatureData.csv";
		try (FileWriter fw = new FileWriter(f)) {
			fw.write("Issuer Id,Mtm Base,Mtm Up,Mtm Down,Delta,Cvr Up, Cvr Down, Cvr");
			fw.write("\n");
			for (Map.Entry<String, EqCurvatureData> me : aggrByIssuer.entrySet()) {
				EqCurvatureData e = me.getValue();
				fw.write(e.getIssuerId() + "," + e.getMtmBase() + "," + e.getMtmUp() + "," + e.getMtmDown() + ","
						+ e.getDelta() + "," + e.getCvrUp() + "," + e.getCvrDown() + "," + e.getCvr());
				fw.write("\n");
			}
		} catch (IOException ioe) {
			log.error(ioe.getMessage(), ioe);
		}
	}

	private void mkdirs(String dir) {
		File f = new File(dir);
		if (!f.exists()) {
			f.mkdirs();
		}
	}
}
