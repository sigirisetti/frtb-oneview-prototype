package com.uob.frtb.risk.samr.model;

import com.uob.frtb.refdata.model.Tenor;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * SAMR Trade data for risk classes IR, IR INFL, IR Basis, FX, EQ, CR and COMM
 * 
 * @author Surya
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString
@Table(name = "frtb_samr_trade_sensitivity")
public class TradeSensitivity extends BaseTradeEntity {

	private static final long serialVersionUID = 3350605260957566776L;

	@Column(name = "sensitivity_type", length = 16, nullable = false)
	@Enumerated(EnumType.STRING)
	protected RiskClassAndSensitivity sensitivityType;

	@Column(name = "rate_index", length = 16)
	private String index;

	@Transient
	private int termCode;

	@Column(name = "term", length = 8)
	private String term;

	@Transient
	private int tenorCode;

	@Column(name = "tenor", length = 8)
	private String tenor;

	@Column(name = "sensitivity")
	private double sensitivity;

	@Column(name = "mtm_base")
	private double mtmBaseAmount;

	@Column(name = "mtm_1bp_up")
	private double mtmBpUpAmount;

	@Column(name = "mtm_1bp_down")
	private double mtmBpDownAmount;

	@Column
	@Type(type = "yes_no")
	private Boolean inflation;

	@Column
	@Type(type = "yes_no")
	private Boolean basis;

	@Column
	@Type(type = "yes_no")
	private Boolean repo;

	/**
	 * Used by IR Delta
	 * 
	 * @param sensitivityType
	 * @param tradeIdentifier
	 * @param indexName
	 * @param currency
	 * @param tenor
	 */
	public TradeSensitivity(RiskClassAndSensitivity sensitivityType, String tradeIdentifier, String indexName,
			String currency, String tenor, double sensitivity) {
		this.sensitivityType = sensitivityType;
		setTradeIdentifier(tradeIdentifier);
		this.index = indexName;
		this.currency = currency;
		this.tenor = tenor;
		this.sensitivity = sensitivity;
	}

	/**
	 * Used by IR Vega
	 * 
	 * @param sensitivityType
	 * @param tradeIdentifier
	 * @param currency
	 * @param term
	 */
	public TradeSensitivity(RiskClassAndSensitivity sensitivityType, String tradeIdentifier, String currency,
			String tenor, Double sensitivity, String term) {
		this.sensitivityType = sensitivityType;
		setTradeIdentifier(tradeIdentifier);
		this.currency = currency;
		this.term = term;
		this.sensitivity = sensitivity;
		this.tenor = tenor;
	}

	/**
	 * Trade Delta Constructor
	 * 
	 * @param sensitivityType
	 * @param tradeIdentifier
	 * @param currency
	 */
	public TradeSensitivity(RiskClassAndSensitivity sensitivityType, String tradeIdentifier, String currency,
			Double sensitivity) {
		this.sensitivityType = sensitivityType;
		setTradeIdentifier(tradeIdentifier);
		this.currency = currency;
		this.sensitivity = sensitivity;
	}

	/**
	 * Trade Vega Constructor
	 * 
	 * @param sensitivityType
	 * @param tradeIdentifier
	 * @param currency
	 * @param tenor
	 */
	public TradeSensitivity(RiskClassAndSensitivity sensitivityType, String tradeIdentifier, String currency,
			String tenor, Double sensitivity) {
		this.sensitivityType = sensitivityType;
		setTradeIdentifier(tradeIdentifier);
		this.currency = currency;
		this.tenor = tenor;
		this.sensitivity = sensitivity;
	}

	/**
	 * Trade Curvature Constructor
	 * 
	 * @param sensitivityType
	 * @param tradeIdentifier
	 * @param currency
	 * @param mtmBase
	 * @param mtmUpShock
	 * @param mtmDownShock
	 */
	public TradeSensitivity(RiskClassAndSensitivity sensitivityType, String tradeIdentifier, String currency,
			Double mtmBase, Double mtmUpShock, Double mtmDownShock, Double sensitivity) {
		this.sensitivityType = sensitivityType;
		setTradeIdentifier(tradeIdentifier);
		this.currency = currency;
		this.mtmBaseAmount = mtmBase;
		this.mtmBpUpAmount = mtmUpShock;
		this.mtmBpDownAmount = mtmDownShock;
		this.sensitivity = sensitivity;
	}

	public int getTenorCode() {
		if (StringUtils.isNotBlank(getTenor())) {
			this.tenorCode = Tenor.parseTenor(getTenor()).getNumberOfDays();
		}
		return tenorCode;
	}

	public int getTermCode() {
		if (StringUtils.isNotBlank(getTerm())) {
			this.termCode = Tenor.parseTenor(getTerm()).getNumberOfDays();
		}
		return termCode;
	}
}
