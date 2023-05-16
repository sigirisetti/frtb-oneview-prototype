package com.uob.frtb.risk.samr.calculators;

import com.uob.frtb.risk.samr.config.DRCNonSecConfig;
import com.uob.frtb.risk.samr.model.DRCNonSec;
import com.uob.frtb.risk.samr.model.FrtbSensitivities;
import com.uob.frtb.risk.samr.model.RiskClass;
import com.uob.frtb.risk.samr.model.Seniority;
import com.uob.frtb.risk.samr.results.PoResults;
import com.uob.frtb.risk.samr.results.RiskClassHierarchyResultRow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Scope("prototype")
public class DefaultRiskChargeNonSecCalc extends BaseRiskChargeCalculator {

	@Autowired
	private DRCNonSecConfig drcNonSecConfig;

	private Map<Integer, List<DRCNonSec>> drcNonSecGroupedByBucket;

	private Map<Integer, DRCNonSecCreditBucketLevelResults> bucketLevelResults;

	@Override
	protected void filter() {
		List<DRCNonSec> list = calcRequest.getDrcNonSecData();
		if (list == null || list.isEmpty()) {
			return;
		}
		drcNonSecGroupedByBucket = list.stream().collect(Collectors.groupingBy(DRCNonSec::getBucket));
	}

	@Override
    protected void calculateRiskCharge() {
		if (drcNonSecGroupedByBucket == null) {
			return;
		}
		bucketLevelResults = new TreeMap<>();
		for (Map.Entry<Integer, List<DRCNonSec>> me : drcNonSecGroupedByBucket.entrySet()) {
			List<DRCNonSecCalcAttrs> attrs = buildCalculatedArttibuteList(me.getKey(), me.getValue());
			Map<String, DRCNonSecGroupedByIssuer> groupedMap = groupByIssuer(attrs);
			Map<Double, IssuerLevelJtdData> issuerLevelJtdData = calculateIssuerLevelJtdData(groupedMap);
			for (Map.Entry<Double, IssuerLevelJtdData> e : issuerLevelJtdData.entrySet()) {
				log.info(" {} - {} ", e.getKey(), e.getValue().toString());
			}
			Map<Integer, RatingLevelJtdData> ratingLevelDrcDataMap = calculateRatingLevelJtdData(issuerLevelJtdData);
			for (Map.Entry<Integer, RatingLevelJtdData> e : ratingLevelDrcDataMap.entrySet()) {
				log.info(" {} - {} ", e.getKey(), e.getValue().toString());
			}
			DRCNonSecCreditBucketLevelResults bucketLevelResult = new DRCNonSecCreditBucketLevelResults(me.getKey(),
					issuerLevelJtdData, ratingLevelDrcDataMap);
			riskCharge += bucketLevelResult.calculateDRC();
			bucketLevelResults.put(me.getKey(), bucketLevelResult);
		}
		log.info("Credit Default Risk charge for non securitized : {}", riskCharge);
	}

	private Map<Integer, RatingLevelJtdData> calculateRatingLevelJtdData(
			Map<Double, IssuerLevelJtdData> issuerLevelJtdData) {
		List<String> ratings = drcNonSecConfig.getCreditRatings();
		Map<Integer, RatingLevelJtdData> ratingLevelDrcDataMap = new TreeMap<>();
		for (IssuerLevelJtdData d : issuerLevelJtdData.values()) {
			int position = ratings.indexOf(d.getRating());
			RatingLevelJtdData ratingLvlData = ratingLevelDrcDataMap.get(position);
			if (ratingLvlData == null) {
				ratingLvlData = RatingLevelJtdData.from(d);
				ratingLevelDrcDataMap.put(position, ratingLvlData);
			} else {
				ratingLvlData.add(d);
			}
		}
		return ratingLevelDrcDataMap;
	}

	private Map<Double, IssuerLevelJtdData> calculateIssuerLevelJtdData(
			Map<String, DRCNonSecGroupedByIssuer> groupedMap) {
		Map<Double, IssuerLevelJtdData> issuerLevelJtdData = new TreeMap<>(Collections.reverseOrder());
		for (Map.Entry<String, DRCNonSecGroupedByIssuer> me : groupedMap.entrySet()) {
			IssuerLevelJtdData o = me.getValue().computeIssuerLevelData();
			issuerLevelJtdData.put(o.getJtd(), o);
		}
		return issuerLevelJtdData;
	}

	private Map<String, DRCNonSecGroupedByIssuer> groupByIssuer(List<DRCNonSecCalcAttrs> attrs) {
		Map<String, DRCNonSecGroupedByIssuer> groupedMap = new HashMap<>();
		for (DRCNonSecCalcAttrs a : attrs) {
			DRCNonSecGroupedByIssuer grouped = groupedMap.get(a.getIssuerId());
			if (grouped == null) {
				grouped = new DRCNonSecGroupedByIssuer();
				groupedMap.put(a.getIssuerId(), grouped);
			}
			grouped.add(a);
		}
		return groupedMap;
	}

	private List<DRCNonSecCalcAttrs> buildCalculatedArttibuteList(Integer key, List<DRCNonSec> value) {
		List<DRCNonSecCalcAttrs> attrs = new ArrayList<>();
		for (DRCNonSec drc : value) {
			DRCNonSecCalcAttrs attr = new DRCNonSecCalcAttrs();
			attr.setIssuerId(drc.getIssuerId());
			attr.setSeniority(drc.getSeniority());
			attr.setRating(drc.getRating());
			attr.setMaturity1(drc.getMaturity() > 1 ? 1 : drc.getMaturity());
			attr.setMaturity2(attr.getMaturity1() < 0.25 ? 0.25 : attr.getMaturity1());
			attr.setPnl(calculatePnl(drc));
			attr.setRw(drcNonSecConfig.getRatingRwMap().get(drc.getRating()));
			attr.setJtd(calculateJtd(drc.getSeniority(), drc.getNotional(), attr.getPnl()));
			attr.setJtdBase(attr.getJtd() * getRate(drc.getCurrency()));
			attr.setMaturityRw(attr.getMaturity2() * attr.getRw());
			attrs.add(attr);
		}
		return attrs;
	}

	private double calculateJtd(String seniority, double notional, double pnl) {
		return drcNonSecConfig.getSeniorityLgdMap().get(seniority.toUpperCase()) * notional + pnl;
	}

	private double calculatePnl(DRCNonSec drc) {
		String product = drc.getProduct().toUpperCase();
		if (product.contains("CDS") || product.contains("FUTURE") || product.contains("FORWARD")) {
			return "LONG".equalsIgnoreCase(drc.getLongOrShort()) ? -Math.abs(drc.getMtm()) : Math.abs(drc.getMtm());
		}
		return drc.getMtm() - drc.getNotional() + drc.getStrike();
	}

	@Override
	void saveResults(PoResults results) {
		log.info("DRC Non Sec - Saving results");
		if (drcNonSecGroupedByBucket == null) {
			return;
		}
		RiskClassHierarchyResultRow deltaResult = new RiskClassHierarchyResultRow(results,
				results.getSamrResults().getCurrency(), results.getSamrResults().getCurrency(),
				FrtbSensitivities.DRC_NON_SEC.toString(), getRiskClass().toString(), 0, riskCharge, 0, 0, 0, 0);
		results.addHierarchyResultRow(deltaResult);
	}

	@Override
	RiskClass getRiskClass() {
		return RiskClass.DEFAULT_RISK;
	}

	@Override
	String getSensitivity() {
		return FrtbSensitivities.DRC_NON_SEC.toString();
	}

	@Getter
	@Setter
	@NoArgsConstructor
	static class DRCNonSecCreditBucketLevelResults {
		private int bucket;
		private Map<Double, IssuerLevelJtdData> issuerLevelJtdData;
		private Map<Integer, RatingLevelJtdData> ratingLevelDrcDataMap;

		private double wts;
		private double defaultRiskCharge;

		public DRCNonSecCreditBucketLevelResults(int bucket, Map<Double, IssuerLevelJtdData> issuerLevelJtdData,
				Map<Integer, RatingLevelJtdData> ratingLevelDrcDataMap) {
			this.bucket = bucket;
			this.issuerLevelJtdData = issuerLevelJtdData;
			this.ratingLevelDrcDataMap = ratingLevelDrcDataMap;

		}

		double calculateDRC() {
			double totalLongJtd = 0;
			double totalShortJtd = 0;
			double totalLongJtdRw = 0;
			double totalShortJtdRw = 0;
			for (Map.Entry<Integer, RatingLevelJtdData> me : ratingLevelDrcDataMap.entrySet()) {
				totalLongJtd += me.getValue().getLongJtd();
				totalShortJtd += me.getValue().getShortJtd();
				totalLongJtdRw += me.getValue().getLongJtdRw();
				totalShortJtdRw += me.getValue().getShortJtdRw();
			}

			wts = calculateWeightedToShortRatio(totalLongJtd, totalShortJtd);
			defaultRiskCharge = Math.max(totalLongJtdRw - wts * Math.abs(totalShortJtdRw), 0);
			return defaultRiskCharge;
		}

		private double calculateWeightedToShortRatio(double totalLongJtd, double totalShortJtd) {
			return totalLongJtd / (totalLongJtd + Math.abs(totalShortJtd));
		}
	}

	@Getter
	@Setter
	@EqualsAndHashCode
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	static class RatingLevelJtdData {
		private String rating;
		private double longJtd;
		private double shortJtd;
		private double longJtdRw;
		private double shortJtdRw;

		static RatingLevelJtdData from(IssuerLevelJtdData o) {
			return new RatingLevelJtdData(o.getRating(), o.getJtdLong(), o.getJtdShort(), o.getRwJtdLong(),
					o.getRwJtdShort());
		}

		void add(IssuerLevelJtdData d) {
			longJtd += d.getJtdLong();
			shortJtd += d.getJtdShort();
			longJtdRw += d.getRwJtdLong();
			shortJtdRw += d.getRwJtdShort();
		}
	}

	@Getter
	@Setter
	@EqualsAndHashCode
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	static class IssuerLevelJtdData {
		private String issuerId;
		private String rating;
		private double jtd;
		private double jtdLong;
		private double jtdShort;
		private double rw;
		private double totalMaturity;
		private double totalMaturityRw;
		private double maturityWeightedRw;
		private double rwJtdLong;
		private double rwJtdShort;

		public void add(DRCNonSecCalcAttrs o) {
			jtd += o.getJtdBase();
			rw = o.getRw();
			totalMaturityRw += o.getMaturityRw();
			totalMaturity += o.getMaturity2();
		}

		public void calculate() {
			maturityWeightedRw = totalMaturityRw / totalMaturity;
			jtdLong = Math.max(jtd, 0);
			jtdShort = Math.min(jtd, 0);
			rwJtdLong = rw * jtdLong;
			rwJtdShort = rw * jtdShort;
		}
	}

	@Getter
	@Setter
	@EqualsAndHashCode
	static class DRCNonSecGroupedByIssuer {
		private Map<String, IssuerLevelJtdData> groupedData = new HashMap<>();

		void add(DRCNonSecCalcAttrs o) {
			IssuerLevelJtdData data = groupedData.get(o.getSeniority());
			if (data == null) {
				data = new IssuerLevelJtdData();
				data.setIssuerId(o.getIssuerId());
				data.setRating(o.getRating());
				data.setJtd(o.getJtd());
				data.setRw(o.getRw());
				data.setTotalMaturity(o.getMaturity2());
				data.setTotalMaturityRw(o.getMaturityRw());
				groupedData.put(o.getSeniority(), data);
			} else {
				data.add(o);
			}
		}

		IssuerLevelJtdData computeIssuerLevelData() {
			double jtdSenior = 0;
			double jtdJunior = 0;
			IssuerLevelJtdData issuerData = new IssuerLevelJtdData();
			for (Map.Entry<String, IssuerLevelJtdData> me : groupedData.entrySet()) {
				issuerData.setIssuerId(me.getValue().getIssuerId());
				issuerData.setRating(me.getValue().getRating());
				issuerData.setRw(me.getValue().getRw());
				if (Seniority.SENIOR.toString().equalsIgnoreCase(me.getKey())) {
					jtdSenior = me.getValue().getJtd();
				} else if (Seniority.JUNIOR.toString().equalsIgnoreCase(me.getKey())) {
					jtdJunior = me.getValue().getJtd();
				}
			}
			issuerData.setJtd(jtdJunior > 0 ? jtdJunior + Math.max(jtdSenior, 0) : jtdJunior + jtdSenior);
			issuerData.calculate();
			return issuerData;
		}
	}

	@Getter
	@Setter
	static class DRCNonSecCalcAttrs {
		private String issuerId;
		private String seniority;
		private String rating;
		private double maturity1;
		private double maturity2;
		private double pnl;
		private double rw;
		private double jtd;
		private double jtdBase;
		private double maturityRw;
	}
}
