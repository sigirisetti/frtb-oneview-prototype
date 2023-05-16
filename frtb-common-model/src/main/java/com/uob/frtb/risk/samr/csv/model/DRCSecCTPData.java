package com.uob.frtb.risk.samr.csv.model;

import com.uob.frtb.risk.samr.model.DRCSecCTP;
import com.uob.frtb.risk.samr.model.RiskClassAndSensitivity;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class DRCSecCTPData extends BaseDRCData<DRCSecCTP> {

	private static final long serialVersionUID = 1L;

	private String index;
	private String region;
	private Integer series;
	private String securitization;

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return RiskClassAndSensitivity.DRC_SEC_CTP;
	}

	@Override
	public List<DRCSecCTP> buildTradeData() {
		return Arrays.asList(buildDRCSecCTP());
	}

	public DRCSecCTP buildDRCSecCTP() {
		DRCSecCTP d = new DRCSecCTP();
		setBaseDRCData(d);
		d.setIndex(index);
		d.setSeries(series);
		d.setRegion(region);
		d.setSecuritization("YES".equalsIgnoreCase(securitization));
		return d;
	}
}
