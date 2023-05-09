package com.quark.risk.frtb.samr.csv.model;

import com.quark.risk.frtb.samr.model.DRCSecNonCTP;
import com.quark.risk.frtb.samr.model.RiskClassAndSensitivity;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class DRCSecNonCTPData extends BaseDRCData<DRCSecNonCTP> {

	private static final long serialVersionUID = 1L;

	private String seniority;
	private String portfolio;
	private String bondOrCds;
	private String category;
	private String region;

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return RiskClassAndSensitivity.DRC_SEC_NON_CTP;
	}

	@Override
	public List<DRCSecNonCTP> buildTradeData() {
		return Arrays.asList(buildDRCSecNonCTP());
	}

	public DRCSecNonCTP buildDRCSecNonCTP() {
		DRCSecNonCTP d = new DRCSecNonCTP();
		setBaseDRCData(d);
		d.setSeniority(seniority);
		d.setPortfolio(portfolio);
		d.setBondOrCds(bondOrCds);
		d.setCategory(category);
		d.setRegion(region);
		return d;
	}
}
