package com.uob.frtb.risk.saccr.results;

import com.uob.frtb.risk.saccr.model.AssetClass;
import com.uob.frtb.risk.saccr.model.BuySell;
import com.uob.frtb.risk.saccr.model.PutCall;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trade implements Serializable {

	private static final long serialVersionUID = -8630567973987081134L;

	private Integer seq;
	private String productName;
	private String contractType;
	private String legalEntity;
	private String laName;
	private String csaName;
	private String po;
	private String tradeId;
	private String sourceSystem;
	private String npvCCY;
	private double npv;
	private Date startDate;
	private Date endDate;
	private Date nextExerciseDate;
	private BuySell buySell;
	private PutCall putCall;
	private String payRateIndex;
	private String recRateIndex;
	private String payoffCcy;
	private double payOffAmount;
	private String notionalCcy;
	private double notional;
	private double quantity;
	private int tickSize;
	private double underlyingPrice;
	private double strike;
	private double quantoFXRate;
	private String payCCY;
	private String recCCY;
	private double payNotional;
	private double recNotional;
	private String subCls;
	private String underlyingProduct;
	private String settlementType;
	private Date earlyTerminationDate;
	private String optionType;
	private double strike2;
	private String exerciseType;

	// -----------------------------

	private AssetClass assetClass;
	private double mpor;
	private double collateralBalance;
	private double haircut;
	private double thresholdAmount;
	private double mta;
	private double nica;
	private double startTenor;
	private double endTenor;
	private double maturityTenor;
	private double nextExecTenor;
	private double supervisoryDuration;
	private double supervisoryFactor;
	private double regulatoryDelta;
	private double maturityFactor;
	private double maturityFactorUn;
	private double adjustedNotional;
	private double adjustedNotionalUn;
	private double reportingNotional;
	private double effectiveNotional;
	private double effectiveNotionalUn;
	private double addOn;
	private double addOnUn;
	private double multiplier;
	private double multiplierUn;
	private double pfe;
	private double pfeUn;
	private double replacementCost;
	private double ead;
	private double eadUn;

	public static String getHeadersString() {
		return StringUtils.join(new Object[] { "product", "contractType", "legalEntity", "laName", "csaName", "po",
				"tradeId", "SourceSystem", "npvCCY", "npv", "startDate", "endDate", "nextExerciseDate", "buySell",
				"putCall", "payRefIndex", "recRefIndex", "payOffCCY", "payOffAmount", "notionalCCY", "notional",
				"quantity", "tickSize", "underlyingPrice", "strike", "quantoFXRate", "payCCY", "recCCY", "payNotional",
				"recNotional", "subCls", "underlyingProduct", "settlementType", "earlyTerminationDate", "optionType",
				"Strike2", "exerciseType", "cls", "MPOR", "Collateral", "Haircut", "THA", "MTA", "NICA", "Start Tenor",
				"End Tenor", "Maturity Tenor", "Next Exec Tenor", "Supervisory Duration", "Supervisory Factor",
				"Regulator Delta", "MF", "MF (U)", "AdjNotional", "AdjNotional(U)", "Reporting Notional", "EffNotional",
				"EffNotional (U)", "AddOn", "AddOn (U)", "Multiplier", "Multiplier(U)", "PFE", "PFE (U)",
				"Replacement Cost", "Replacement Cost (U)", "EAD", "EAD (U)" }, ",");
	}

	public String getCommaSeperatedString() {
		return StringUtils.join(new Object[] { productName, contractType, legalEntity, laName, csaName, po, tradeId,
				sourceSystem, npvCCY, npv, startDate, endDate, nextExerciseDate, buySell, putCall, payRateIndex,
				recRateIndex, payoffCcy, payOffAmount, notionalCcy, notional, quantity, tickSize, underlyingPrice,
				strike, quantoFXRate, payCCY, recCCY, payNotional, recNotional, subCls, underlyingProduct,
				settlementType, earlyTerminationDate, optionType, strike2, exerciseType, assetClass, mpor,
				collateralBalance, haircut, thresholdAmount, mta, nica, startTenor, endTenor, maturityTenor,
				nextExecTenor, supervisoryDuration, supervisoryFactor, regulatoryDelta, maturityFactor,
				maturityFactorUn, adjustedNotional, adjustedNotionalUn, reportingNotional, effectiveNotional,
				effectiveNotionalUn, addOn, addOnUn, multiplier, multiplierUn, pfe, pfeUn, replacementCost,
				replacementCost, ead, eadUn }, ",");
	}
}
