package com.quark.risk.frtb.samr.csv.reader;

import com.quark.common.dao.CoreDao;
import com.quark.risk.frtb.samr.csv.model.CRNonSecCurvatureData;
import com.quark.risk.frtb.samr.model.TradeSensitivity;

public class CRNonSecCurvatureCsvReader  extends CurvatureCsvReader<TradeSensitivity, CRNonSecCurvatureData> {

	public CRNonSecCurvatureCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	@Override
	public Class<CRNonSecCurvatureData> getCsvReaderType() {
		return CRNonSecCurvatureData.class;
	}
}
