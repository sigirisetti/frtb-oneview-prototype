package com.uob.frtb.risk.saccr.csv.model;

import com.uob.frtb.risk.saccr.model.Collateral;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CollateralData extends SACCRData<Collateral> {

	private static final long serialVersionUID = -7328513568841945604L;

	private String key;
	private String counterparty;
	private String laId;
	private String csaId;
	private String collateralBalance;
	private String haircut;
	private String thresholdForPo;
	private String thresholdForCounterparty;
	private String mtaForPo;
	private String mtaForCounterparty;
	private String iaForPo;
	private String iaForCounterparty;
	private String nica;
	private String collateralCallFrequency;
	private String dispute;
	private String liquid;
	private String clearing;

	public Collateral build() {
		Collateral c = new Collateral();
		if (StringUtils.isNotBlank(key)) {
			c.setKey(key);
		} else {
			addMessage("Collateral key is missing");
			return null;
		}
		c.setCounterparty(counterparty);
		c.setLaId(laId);
		c.setCsaId(csaId);
		if (NumberUtils.isNumber(collateralBalance)) {
			c.setCollateralBalance(new Double(collateralBalance));
		} else {
			addMessage("Collateral Balance is not a number");
		}
		if (NumberUtils.isNumber(haircut)) {
			c.setHaircut(new Double(haircut));
		} else {
			addMessage("Haircut is not a number");
		}
		if (NumberUtils.isNumber(thresholdForPo)) {
			c.setThresholdForPo(new Double(thresholdForPo));
		} else {
			addMessage("PO ThresholdFor is not a number");
		}
		if (NumberUtils.isNumber(thresholdForCounterparty)) {
			c.setThresholdForCounterparty(new Double(thresholdForCounterparty));
		} else {
			addMessage("Counterparty ThresholdFor is not a number");
		}
		if (NumberUtils.isNumber(mtaForPo)) {
			c.setMtaForPo(new Double(mtaForPo));
		} else {
			addMessage("PO MTA is not a number");
		}
		if (NumberUtils.isNumber(mtaForCounterparty)) {
			c.setMtaForCounterparty(new Double(mtaForCounterparty));
		} else {
			addMessage("Counterparty MTA is not a number");
		}
		if (NumberUtils.isNumber(iaForPo)) {
			c.setIaForPo(new Double(iaForPo));
		} else {
			addMessage("PO IA is not a number");
		}
		if (NumberUtils.isNumber(iaForCounterparty)) {
			c.setIaForCounterparty(new Double(iaForCounterparty));
		} else {
			addMessage("Counterparty IA is not a number");
		}
		if (NumberUtils.isNumber(nica)) {
			c.setNica(new Double(nica));
		} else {
			addMessage("NICA is not a number");
		}
		if (NumberUtils.isNumber(collateralCallFrequency)) {
			c.setCollateralCallFrequency(new Integer(collateralCallFrequency));
		} else {
			addMessage("Collateral call freq is not a number");
		}
		c.setDispute("Y".equalsIgnoreCase(dispute));
		c.setLiquid("Y".equalsIgnoreCase(liquid));
		c.setClearing("Y".equalsIgnoreCase(clearing));
		return c;
	}
}
