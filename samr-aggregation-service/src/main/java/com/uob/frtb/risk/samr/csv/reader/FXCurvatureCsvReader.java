package com.uob.frtb.risk.samr.csv.reader;

import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.risk.samr.csv.model.FXCurvatureData;
import com.uob.frtb.risk.samr.model.TradeSensitivity;

public class FXCurvatureCsvReader extends CurvatureCsvReader<TradeSensitivity, FXCurvatureData> {

	public FXCurvatureCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	@Override
	public Class<FXCurvatureData> getCsvReaderType() {
		return FXCurvatureData.class;
	}
}
