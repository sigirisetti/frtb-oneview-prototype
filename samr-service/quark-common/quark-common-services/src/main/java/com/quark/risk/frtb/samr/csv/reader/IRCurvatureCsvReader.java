package com.quark.risk.frtb.samr.csv.reader;

import com.quark.common.dao.CoreDao;
import com.quark.risk.frtb.samr.csv.model.IRCurvatureData;
import com.quark.risk.frtb.samr.model.TradeSensitivity;

public class IRCurvatureCsvReader extends CurvatureCsvReader<TradeSensitivity, IRCurvatureData> {

	public IRCurvatureCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	@Override
	public Class<IRCurvatureData> getCsvReaderType() {
		return IRCurvatureData.class;
	}
}
