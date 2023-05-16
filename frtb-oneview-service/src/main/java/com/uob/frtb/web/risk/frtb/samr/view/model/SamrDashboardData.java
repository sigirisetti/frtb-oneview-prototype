package com.uob.frtb.web.risk.frtb.samr.view.model;

import com.uob.frtb.risk.common.model.WorkflowInstance;
import com.uob.frtb.risk.frtb.samr.results.RiskClassLevelResults;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SamrDashboardData {
	private Date valueDate;
	private WorkflowInstance workflowInstance;
	private List<RiskClassLevelResults> riskClassLevelResults;

	/*
	public List<LabelValueData> getRiskClassPieChartData() {
		List<LabelValueData> labelValueData = new ArrayList<>();
		if (riskClassLevelResults != null) {
			for (RiskClassLevelResults r : riskClassLevelResults) {
				labelValueData.add(new LabelValueData(r.getRiskClass().toString(), r.getTotalRiskCharge()));
			}
		}
		return labelValueData;
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
					delta.addBar(new LabelValueData(r.getRiskClass().toString(), 0.0));
					vega.addBar(new LabelValueData(r.getRiskClass().toString(), 0.0));
					curv.addBar(new LabelValueData(r.getRiskClass().toString(), 0.0));
					drcNonSec.addBar(new LabelValueData(r.getRiskClass().toString(), r.getDrcNonSec()));
					drcSecNonCtp.addBar(new LabelValueData(r.getRiskClass().toString(), r.getDrcSecNonCtp()));
					drcSecCtp.addBar(new LabelValueData(r.getRiskClass().toString(), r.getDrcSecCtp()));
				} else {
					delta.addBar(new LabelValueData(r.getRiskClass().toString(), r.getDelta()));
					vega.addBar(new LabelValueData(r.getRiskClass().toString(), r.getVega()));
					curv.addBar(new LabelValueData(r.getRiskClass().toString(), r.getCurvature()));
					drcNonSec.addBar(new LabelValueData(r.getRiskClass().toString(), 0.0));
					drcSecNonCtp.addBar(new LabelValueData(r.getRiskClass().toString(), 0.0));
					drcSecCtp.addBar(new LabelValueData(r.getRiskClass().toString(), 0.0));
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
    */
}
