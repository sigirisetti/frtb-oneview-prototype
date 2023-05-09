package com.quark.risk.frtb.samr.csv.reader;

import com.quark.common.dao.CoreDao;
import com.quark.risk.frtb.samr.csv.model.FXCurvatureData;
import com.quark.risk.frtb.samr.model.TradeSensitivity;

public class FXCurvatureCsvReader extends CurvatureCsvReader<TradeSensitivity, FXCurvatureData> {

	public FXCurvatureCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	@Override
	public Class<FXCurvatureData> getCsvReaderType() {
		return FXCurvatureData.class;
	}
}
