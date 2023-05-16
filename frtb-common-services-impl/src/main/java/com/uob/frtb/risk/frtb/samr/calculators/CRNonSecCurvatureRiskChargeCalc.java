package com.uob.frtb.risk.frtb.samr.calculators;

import com.uob.frtb.risk.frtb.samr.config.CRNonSecCurvatureConfig;
import com.uob.frtb.risk.frtb.samr.model.CreditIssuerInfo;
import com.uob.frtb.risk.frtb.samr.model.FrtbSensitivities;
import com.uob.frtb.risk.frtb.samr.model.RiskClass;
import com.uob.frtb.risk.frtb.samr.model.RiskClassAndSensitivity;
import com.uob.frtb.risk.frtb.samr.model.TradeSensitivity;
import com.uob.frtb.risk.frtb.samr.results.PoResults;
import com.uob.frtb.risk.frtb.samr.results.RiskClassHierarchyResultRow;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope("prototype")
public class CRNonSecCurvatureRiskChargeCalc extends BaseCRNonSecRiskChargeCalc {

	@Autowired
	private CRNonSecCurvatureConfig config;

	private Map<CRCurrencyDeltaKey, CRCurrencyDelta> currDeltaMap = new TreeMap<>();
	private Map<String, CRCurrencyCurvature> currCurvMap = new TreeMap<>();

	@Override
	protected void filter() {
		Map<String, CreditIssuerInfo> issuerInfo = getTradeIdCreditIssuerMap();
		for (TradeSensitivity sens : calcRequest.getTradeSensitivities()) {
			if (RiskClassAndSensitivity.CR_NS_CURVATURE != sens.getSensitivityType()) {
				continue;
			}
			CreditIssuerInfo info = issuerInfo.get(sens.getTradeIdentifier());
			if (info == null) {
				log.warn("Credit Issuer info not found for trade : {}", sens.getTradeIdentifier());
				continue;
			}
			CRCurrencyDeltaKey key = new CRCurrencyDeltaKey(info.getBucket(), info.getCurrency(), info.getIssuerId());
			if (currDeltaMap.containsKey(key)) {

			}
		}
	}

	@Override
    protected void calculateRiskCharge() {
	}

	@Getter
	@Setter
	@AllArgsConstructor
	private class CRCurrencyCurvature {
		private String currency;
		private String Issuer;
		private double cvrUp;
		private double cvr;
		private double cvrDown;
		private double kb;
		private double kbBase;
		private double sb;
	}

	CRCurrencyCurvature fromCurrencyDeltas(CRCurrencyDelta deltas) {
		double cvrUp = getCvrUp(deltas);
		double cvrDown = getCvrDown(deltas);
		double cvr = -Math.min(cvrUp, cvrDown);
		double kb = Math.abs(cvr);
		double kbBase = kb * getRate(deltas.getKey().getCurrency());
		// return new CRCurrencyCurvature(deltas.getKey().getCurrency(), cvrUp,
		// cvrDown, cvr, kb, kbBase, kbBase);
		return null;
	}

	double getCvrUp(CRCurrencyDelta deltas) {
		return (deltas.getMtmUp() - deltas.getMtmBase()) - deltas.getDelta();
	}

	double getCvrDown(CRCurrencyDelta deltas) {
		return (deltas.getMtmDown() - deltas.getMtmBase()) + deltas.getDelta();
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@ToString
	private class CRCurrencyDelta {
		private CRCurrencyDeltaKey key;
		private double mtmUp;
		private double mtmBase;
		private double mtmDown;
		private double delta;

		void add(CRCurrencyDelta d) {
			this.mtmUp += d.mtmUp;
			this.mtmBase += d.mtmBase;
			this.mtmDown += d.mtmDown;
			this.delta += d.delta;
		}
	}

	@Override
	void saveResults(PoResults results) {
		log.info("IR Curvature - Saving sensitivity results");
		if (currCurvMap.isEmpty()) {
			return;
		}
		RiskClassHierarchyResultRow curvatureResult = new RiskClassHierarchyResultRow(results,
				results.getSamrResults().getCurrency(), results.getSamrResults().getCurrency(),
				FrtbSensitivities.CURVATURE.toString(), getRiskClass().toString(), 0, riskCharge, 0, 0, 0, 0);
		results.addHierarchyResultRow(curvatureResult);
		for (Map.Entry<String, CRCurrencyCurvature> me : currCurvMap.entrySet()) {
			log.debug("Saving results for currency : {}", me.getKey());
			RiskClassHierarchyResultRow curvatureCurrResult = new RiskClassHierarchyResultRow(results, me.getKey(),
					results.getSamrResults().getCurrency(), me.getKey(), curvatureResult.buildParentKey(),
					me.getValue().getKb(), me.getValue().getKbBase(), 0, 0, 0, 0);
			results.addHierarchyResultRow(curvatureCurrResult);
		}
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@EqualsAndHashCode
	private class CRCurrencyDeltaKey implements Comparable<CRCurrencyDeltaKey> {
		private int bucket;
		private String currency;
		private String issuer;

		@Override
		public int compareTo(CRCurrencyDeltaKey o) {
			return new CompareToBuilder().append(this.bucket, o.bucket).append(this.currency, o.currency)
					.append(this.issuer, o.issuer).toComparison();
		}
	}

	@Override
	String getSensitivity() {
		return FrtbSensitivities.CURVATURE.toString();
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.CREDIT_NS;
	}

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return RiskClassAndSensitivity.CR_NS_CURVATURE;
	}

	@Override
	protected double getSensFactor() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int[] getTenorCodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected double getRw(int bucket) {
		// TODO Auto-generated method stub
		return 0;
	}
}
