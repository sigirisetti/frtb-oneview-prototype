package com.uob.frtb.risk.saccr.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VBA {

	public void calculateSACCR(boolean incremental) {
		Date valueDate;
		double alpha;
		String reportingCcy;
		char OffSetAcrMatBuck;
		String PortfolioName;
		
		Map<String, String> multipleCSAToSingleNettingSet = new HashMap<>();
		Map<String, String> singleCSAToMultipleNettingSet = new HashMap<>();
		Map<String, String> unmarginedNettingSet = new HashMap<>();
		
		Map<String, String> multipleCSAToSingleNettingSetFinal = new HashMap<>();
		Map<String, String> singleCSAToMultipleNettingSetFinal = new HashMap<>();
		Map<String, String> unmarginedNettingSetFinal = new HashMap<>();
		Map<String, String> counterpartyNettingSetFinal = new HashMap<>();
		Map<String, String> nettingSetConfig = new HashMap<>();
		Map<String, String> multipleNettingSets = new HashMap<>();
	}
}
