package com.uob.frtb.risk.samr.csv.reader;

import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.risk.samr.csv.model.CRNonSecCurvatureData;
import com.uob.frtb.risk.samr.model.TradeSensitivity;

public class CRNonSecCurvatureCsvReader  extends CurvatureCsvReader<TradeSensitivity, CRNonSecCurvatureData> {

	public CRNonSecCurvatureCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	@Override
	public Class<CRNonSecCurvatureData> getCsvReaderType() {
		return CRNonSecCurvatureData.class;
	}
}
