package com.uob.frtb.risk.frtb.samr.csv.model;

import com.uob.frtb.risk.frtb.samr.model.DRCNonSec;
import com.uob.frtb.risk.frtb.samr.model.RiskClassAndSensitivity;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true, includeFieldNames = true)
public class DRCNonSecData extends BaseDRCData<DRCNonSec> {

	private static final long serialVersionUID = 1L;

	private String seniority;
	private String issuerId;
	private String issuer;
	private double strike;

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return RiskClassAndSensitivity.DRC_NON_SEC;
	}

	@Override
	public List<DRCNonSec> buildTradeData() {
		return Arrays.asList(buildDRCNonSec());
	}

	public DRCNonSec buildDRCNonSec() {
		DRCNonSec e = new DRCNonSec();
		setBaseDRCData(e);
		e.setSeniority(seniority);
		e.setIssuerId(issuerId);
		e.setIssuer(issuer);
		e.setStrike(strike);
		return e;
	}
}
