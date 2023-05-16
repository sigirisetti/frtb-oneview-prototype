package com.uob.frtb.risk.frtb.samr.csv.reader;

import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.risk.frtb.samr.csv.model.IRCurvatureData;
import com.uob.frtb.risk.frtb.samr.model.TradeSensitivity;

public class IRCurvatureCsvReader extends CurvatureCsvReader<TradeSensitivity, IRCurvatureData> {

	public IRCurvatureCsvReader(String csvName, CoreDao coreDao) {
		super(csvName, coreDao);
	}

	@Override
	public Class<IRCurvatureData> getCsvReaderType() {
		return IRCurvatureData.class;
	}
}
