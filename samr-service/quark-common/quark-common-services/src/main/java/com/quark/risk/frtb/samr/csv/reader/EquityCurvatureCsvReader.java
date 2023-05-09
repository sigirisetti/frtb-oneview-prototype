package com.quark.risk.frtb.samr.csv.reader;

import com.quark.common.dao.CoreDao;
import com.quark.risk.frtb.samr.csv.model.EQCurvatureData;
import com.quark.risk.frtb.samr.model.TradeSensitivity;

public class EquityCurvatureCsvReader extends CurvatureCsvReader<TradeSensitivity, EQCurvatureData> {

	public EquityCurvatureCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	@Override
	public Class<EQCurvatureData> getCsvReaderType() {
		return EQCurvatureData.class;
	}
}
