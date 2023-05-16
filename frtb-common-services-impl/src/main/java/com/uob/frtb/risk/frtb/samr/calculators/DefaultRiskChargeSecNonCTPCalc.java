package com.uob.frtb.risk.frtb.samr.calculators;

import com.uob.frtb.risk.frtb.samr.config.DRCSecNonCTPConfig;
import com.uob.frtb.risk.frtb.samr.model.DRCSecNonCTP;
import com.uob.frtb.risk.frtb.samr.model.FrtbSensitivities;
import com.uob.frtb.risk.frtb.samr.model.RiskClass;
import com.uob.frtb.risk.frtb.samr.results.PoResults;
import com.uob.frtb.risk.frtb.samr.results.RiskClassHierarchyResultRow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope("prototype")
public class DefaultRiskChargeSecNonCTPCalc extends BaseRiskChargeCalculator {

	private static final String SENIOR = "Senior";

	@Autowired
	private DRCSecNonCTPConfig drcSecNonCTPConfig;

	private Map<Integer, BucketLevelJtdData> bucketLevelJtdData;

	@Override
	protected void filter() {
	}

	@Override
    protected void calculateRiskCharge() {
		if (calcRequest.getDrcSecNonCTPData().isEmpty()) {
			return;
		}
		List<DRCSecNonCTPCalcAttrs> attrs = buildCalculatedArttibuteList();
		bucketLevelJtdData = new TreeMap<>();
		for (DRCSecNonCTPCalcAttrs attr : attrs) {
			BucketLevelJtdData dat = bucketLevelJtdData.get(attr.getBucket());
			if (dat == null) {
				dat = new BucketLevelJtdData();
				dat.setBucket(attr.getBucket());
				bucketLevelJtdData.put(attr.getBucket(), dat);
			}
			dat.add(attr);
		}
		for (Map.Entry<Integer, BucketLevelJtdData> me : bucketLevelJtdData.entrySet()) {
			me.getValue().calculateDrc();
			riskCharge += me.getValue().getDrc();
		}
	}

	private List<DRCSecNonCTPCalcAttrs> buildCalculatedArttibuteList() {
		List<DRCSecNonCTPCalcAttrs> attrs = new ArrayList<>();
		for (DRCSecNonCTP drc : calcRequest.getDrcSecNonCTPData()) {
			DRCSecNonCTPCalcAttrs attr = new DRCSecNonCTPCalcAttrs();
			attr.setBucket(drc.getBucket());
			attr.setPortfolio(drc.getPortfolio());
			attr.setSeniority(drc.getSeniority());
			attr.setRating(drc.getRating());
			attr.setMaturity1(drc.getMaturity() > 1 ? 1 : drc.getMaturity());
			attr.setMaturity2(1);
			attr.setPnl(calculatePnl(drc));
			attr.setRw(deriveRiskWeight(drc));
			attr.setJtd(drc.getNotional() + attr.getPnl());
			attr.setJtdBase(attr.getJtd() * getRate(drc.getCurrency()));
			attrs.add(attr);
		}
		return attrs;
	}

	private double deriveRiskWeight(DRCSecNonCTP drc) {
		if (SENIOR.equalsIgnoreCase(drc.getSeniority())) {
			return drcSecNonCTPConfig.getDrcSeniorRatingRwMap().get(drc.getRating());
		} else {
			return drcSecNonCTPConfig.getDrcNonSeniorRatingRwMap().get(drc.getRating());
		}
	}

	private double calculatePnl(DRCSecNonCTP drc) {
		return drc.getMtm() - drc.getNotional();
	}

	@Override
	void saveResults(PoResults results) {
		log.info("DRC Sec Non CTP - Saving results");
		if (calcRequest.getDrcSecNonCTPData().isEmpty()) {
			return;
		}
		RiskClassHierarchyResultRow deltaResult = new RiskClassHierarchyResultRow(results,
				results.getSamrResults().getCurrency(), results.getSamrResults().getCurrency(),
				FrtbSensitivities.DRC_SEC_NON_CTP.toString(), getRiskClass().toString(), 0, riskCharge, 0, 0, 0, 0);
		results.addHierarchyResultRow(deltaResult);
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.DEFAULT_RISK;
	}

	@Override
	String getSensitivity() {
		return FrtbSensitivities.DRC_SEC_NON_CTP.toString();
	}

	@Getter
	@Setter
	static class BucketLevelJtdData {
		private int bucket;
		private double longJtd;
		private double shortJtd;
		private double wts;
		private double rwLongJtd;
		private double rwShortJtd;
		private double drc;

		void add(DRCSecNonCTPCalcAttrs data) {
			this.longJtd += data.getLongJtd();
			this.shortJtd += data.getShortJtd();
			this.rwLongJtd += data.getRwLongJtd();
			this.rwShortJtd += data.getRwShortJtd();
		}

		void calculateDrc() {
			this.wts = this.longJtd / (this.longJtd + Math.abs(this.shortJtd));
			this.drc = this.rwLongJtd - this.wts * Math.abs(this.rwShortJtd);
		}
	}

	@Getter
	@Setter
	static class DRCSecNonCTPCalcAttrs {
		private int bucket;
		private String portfolio;
		private String seniority;
		private String rating;
		private double maturity1;
		private double maturity2;
		private double pnl;
		private double rw;
		private double jtd;
		private double jtdBase;
		private double longJtd;
		private double shortJtd;
		private double rwLongJtd;
		private double rwShortJtd;

		void setJtdBase(double d) {
			this.jtdBase = d;
			this.longJtd = Math.max(jtdBase, 0);
			this.shortJtd = Math.min(jtdBase, 0);
			this.rwLongJtd = this.longJtd * rw;
			this.rwShortJtd = this.shortJtd * rw;
		}
	}
}
