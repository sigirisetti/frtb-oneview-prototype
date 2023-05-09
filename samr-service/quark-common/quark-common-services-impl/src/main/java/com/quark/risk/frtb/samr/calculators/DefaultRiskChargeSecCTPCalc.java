package com.quark.risk.frtb.samr.calculators;

import com.quark.risk.frtb.samr.config.DRCSecCTPConfig;
import com.quark.risk.frtb.samr.model.DRCSecCTP;
import com.quark.risk.frtb.samr.model.FrtbSensitivities;
import com.quark.risk.frtb.samr.model.RiskClass;
import com.quark.risk.frtb.samr.results.PoResults;
import com.quark.risk.frtb.samr.results.RiskClassHierarchyResultRow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope("prototype")
public class DefaultRiskChargeSecCTPCalc extends BaseRiskChargeCalculator {

	@Autowired
	private DRCSecCTPConfig drcSecCTPConfig;

	private Map<Integer, BucketLevelJtdData> bucketLevelJtdData;

	@Override
	protected void filter() {
	}

	@Override
    protected void calculateRiskCharge() {
		if (calcRequest.getDrcSecCTPData().isEmpty()) {
			return;
		}
		List<DRCSecCTPCalcAttrs> attrs = buildCalculatedArttibuteList();
		bucketLevelJtdData = new TreeMap<>();
		for (DRCSecCTPCalcAttrs attr : attrs) {
			BucketLevelJtdData dat = bucketLevelJtdData.get(attr.getBucket());
			if (dat == null) {
				dat = new BucketLevelJtdData();
				dat.setBucket(attr.getBucket());
				bucketLevelJtdData.put(attr.getBucket(), dat);
			}
			dat.add(attr);
		}
		double totalMaxDrc = 0;
		double totalMinDrc = 0;
		for (Map.Entry<Integer, BucketLevelJtdData> me : bucketLevelJtdData.entrySet()) {
			me.getValue().calculateDrc();
			totalMaxDrc += me.getValue().getMaxDrc();
			totalMinDrc += me.getValue().getMinDrc();
			log.info(me.getValue().toString());
		}
		riskCharge = Math.max(totalMaxDrc + 0.5 * totalMinDrc, 0);
	}

	private List<DRCSecCTPCalcAttrs> buildCalculatedArttibuteList() {
		List<DRCSecCTPCalcAttrs> attrs = new ArrayList<>();
		for (DRCSecCTP drc : calcRequest.getDrcSecCTPData()) {
			DRCSecCTPCalcAttrs attr = new DRCSecCTPCalcAttrs();
			attr.setBucket(drc.getBucket());
			attr.setIndex(drc.getIndex());
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

	private double deriveRiskWeight(DRCSecCTP drc) {
		return drcSecCTPConfig.getDrcRatingRwMap().get(drc.getRating());
	}

	private double calculatePnl(DRCSecCTP drc) {
		return drc.getMtm();
	}

	@Override
	void saveResults(PoResults results) {
		log.info("DRC Sec CTP - Saving results");
		if (calcRequest.getDrcSecCTPData().isEmpty()) {
			return;
		}
		RiskClassHierarchyResultRow deltaResult = new RiskClassHierarchyResultRow(results,
				results.getSamrResults().getCurrency(), results.getSamrResults().getCurrency(),
				FrtbSensitivities.DRC_SEC_CTP.toString(), getRiskClass().toString(), 0, riskCharge, 0, 0, 0, 0);
		results.addHierarchyResultRow(deltaResult);
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.DEFAULT_RISK;
	}

	@Override
	String getSensitivity() {
		return FrtbSensitivities.DRC_SEC_CTP.toString();
	}

	@Getter
	@Setter
	@ToString
	static class BucketLevelJtdData {
		private int bucket;
		private double longJtd;
		private double shortJtd;
		private double wts;
		private double rwLongJtd;
		private double rwShortJtd;
		private double drc;
		private double maxDrc;
		private double minDrc;

		void add(DRCSecCTPCalcAttrs data) {
			this.longJtd += data.getLongJtd();
			this.shortJtd += data.getShortJtd();
			this.rwLongJtd += data.getRwLongJtd();
			this.rwShortJtd += data.getRwShortJtd();
		}

		void calculateDrc() {
			this.wts = this.longJtd / (this.longJtd + Math.abs(this.shortJtd));
			this.drc = this.rwLongJtd - this.wts * Math.abs(this.rwShortJtd);
			this.maxDrc = Math.max(drc, 0);
			this.minDrc = Math.min(drc, 0);
		}
	}

	@Getter
	@Setter
	static class DRCSecCTPCalcAttrs {
		private int bucket;
		private String index;
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
