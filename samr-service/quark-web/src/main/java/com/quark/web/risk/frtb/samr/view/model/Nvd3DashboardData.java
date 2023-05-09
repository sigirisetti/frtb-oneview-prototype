package com.quark.web.risk.frtb.samr.view.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quark.risk.common.model.WorkflowInstance;
import com.quark.risk.frtb.samr.model.FrtbSensitivities;
import com.quark.risk.frtb.samr.model.RiskClass;
import com.quark.risk.frtb.samr.results.RiskClassLevelResults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Nvd3DashboardData {
	private Date valueDate;
	private WorkflowInstance workflowInstance;
	@JsonIgnore
	private List<RiskClassLevelResults> riskClassLevelResults;

	public List<PieChartData> getRiskClassPieChartData() {
		List<PieChartData> pieChartData = new ArrayList<>();
		if (riskClassLevelResults != null) {
			for (RiskClassLevelResults r : riskClassLevelResults) {
				pieChartData.add(new PieChartData(r.getRiskClass().toString(), r.getTotalRiskCharge()));
			}
		}
		return pieChartData;
	}

	public List<ChartData> getRiskClassBarChartData() {
		ChartData delta = new ChartData(FrtbSensitivities.DELTA.toString());
		ChartData vega = new ChartData(FrtbSensitivities.VEGA.toString());
		ChartData curv = new ChartData(FrtbSensitivities.CURVATURE.toString());
		ChartData drcNonSec = new ChartData(FrtbSensitivities.DRC_NON_SEC.toString());
		ChartData drcSecNonCtp = new ChartData(FrtbSensitivities.DRC_SEC_NON_CTP.toString());
		ChartData drcSecCtp = new ChartData(FrtbSensitivities.DRC_SEC_CTP.toString());
		if (riskClassLevelResults != null) {
			for (RiskClassLevelResults r : riskClassLevelResults) {
				if (RiskClass.DEFAULT_RISK.equals(r.getRiskClass())) {
					delta.addBar(new KeyValue(r.getRiskClass().toString(), 0));
					vega.addBar(new KeyValue(r.getRiskClass().toString(), 0));
					curv.addBar(new KeyValue(r.getRiskClass().toString(), 0));
					drcNonSec.addBar(new KeyValue(r.getRiskClass().toString(), r.getDrcNonSec()));
					drcSecNonCtp.addBar(new KeyValue(r.getRiskClass().toString(), r.getDrcSecNonCtp()));
					drcSecCtp.addBar(new KeyValue(r.getRiskClass().toString(), r.getDrcSecCtp()));
				} else {
					delta.addBar(new KeyValue(r.getRiskClass().toString(), r.getDelta()));
					vega.addBar(new KeyValue(r.getRiskClass().toString(), r.getVega()));
					curv.addBar(new KeyValue(r.getRiskClass().toString(), r.getCurvature()));
					drcNonSec.addBar(new KeyValue(r.getRiskClass().toString(), 0));
					drcSecNonCtp.addBar(new KeyValue(r.getRiskClass().toString(), 0));
					drcSecCtp.addBar(new KeyValue(r.getRiskClass().toString(), 0));
				}
			}
		}
		return Arrays.asList(delta, vega, curv, drcNonSec, drcSecNonCtp, drcSecCtp);
	}

	public List<ChartData> getRiskClassTimeSeriesData() {
		return Collections.emptyList();
	}

	public List<ChartData> getTotalMarginChartData() {
		return Collections.emptyList();
	}
}
