package com.uob.frtb.risk.samr.calculators;

import com.uob.frtb.common.dao.CoreDao;
import com.uob.frtb.core.exception.ApplicationException;
import com.uob.frtb.risk.samr.model.CalcRequest;
import com.uob.frtb.risk.samr.model.SamrMarketQuote;
import com.uob.frtb.risk.samr.model.RiskClass;
import com.uob.frtb.risk.samr.results.PoResults;

import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
abstract class BaseRiskChargeCalculator {

	protected NumberFormat nf = new DecimalFormat("###,###,###,###,###.00");

	protected CalcRequest calcRequest;

	protected double riskCharge;
	protected List<String> messages = new ArrayList<>();

	@Autowired
	protected CoreDao coreDao;

	protected abstract void filter();

	protected abstract void calculateRiskCharge() throws ApplicationException;

	protected double getRate(String currency) {
		String qn = "FX.".concat(currency).concat(".").concat(calcRequest.getBaseCurrency());
		for (SamrMarketQuote q : calcRequest.getQuotes()) {
			if (qn.equals(q.getQuoteName())) {
				return q.getQuoteValue();
			}
		}
		logError(qn);
		return 1;
	}

	private void logError(String qn) {
		String errMsg = String.format("No fx quote for name '%s'", qn);
		log.error(errMsg);
		messages.add(errMsg);
	}

	protected double calculateRiskCharge(double crossBucketCorr, List<Double> kb, List<Double> sb) {
		double d = 0;
		for (int i = 0; i < kb.size(); i++) {
			d += kb.get(i) * kb.get(i);
			for (int j = 0; j < kb.size(); j++) {
				if (i != j) {
					d += sb.get(i) * sb.get(j) * crossBucketCorr;
				}
			}
		}
		return Math.sqrt(d);
	}

	abstract void saveResults(PoResults results);

	abstract String getSensitivity();

	abstract RiskClass getRiskClass();
}
