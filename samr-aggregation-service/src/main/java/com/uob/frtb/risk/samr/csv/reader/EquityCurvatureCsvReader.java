package com.uob.frtb.risk.samr.csv.reader;

import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.risk.samr.csv.model.EQCurvatureData;
import com.uob.frtb.risk.samr.model.TradeSensitivity;

public class EquityCurvatureCsvReader extends CurvatureCsvReader<TradeSensitivity, EQCurvatureData> {

	public EquityCurvatureCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	@Override
	public Class<EQCurvatureData> getCsvReaderType() {
		return EQCurvatureData.class;
	}
}
