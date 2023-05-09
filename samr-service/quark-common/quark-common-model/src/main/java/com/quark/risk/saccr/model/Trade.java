package com.quark.risk.saccr.model;

import com.quark.risk.common.model.BaseRiskEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "saccr_trade")
public class Trade extends BaseRiskEntity {

	private static final long serialVersionUID = 434669646130779666L;

	@Column(name = "seq", nullable = false)
	private Integer seq;

	@Column(name = "product_name", length = 64, nullable = false)
	private String productName;

	@Column(name = "contract_type", length = 16, nullable = false)
	private String contractType;

	@Column(name = "legal_entity", length = 64, nullable = false)
	private String legalEntity;

	@Column(name = "la_name", length = 64)
	private String laName;

	@Column(name = "csa_name", length = 64)
	private String csaName;

	@Column(name = "po", length = 64, nullable = false)
	private String po;

	@Column(name = "trade_id", length = 64, nullable = false)
	private String tradeId;

	@Column(name = "source_system", length = 64, nullable = false)
	private String sourceSystem;

	@Column(name = "npv_ccy", length = 3, nullable = false)
	private String npvCCY;

	@Column(name = "npv", nullable = false)
	private double npv;

	@Column(name = "start_date", nullable = false)
	private Date startDate;

	@Column(name = "end_date", nullable = false)
	private Date endDate;

	@Column(name = "next_exercise_date")
	private Date nextExerciseDate;

	@Column(name = "buy_sell", length = 4)
	@Enumerated(EnumType.STRING)
	private BuySell buySell;

	@Column(name = "put_call", length = 4)
	@Enumerated(EnumType.STRING)
	private PutCall putCall;

	@Column(name = "pay_rate_idx", length = 16)
	private String payRateIndex;

	@Column(name = "rec_rate_idx", length = 16)
	private String recRateIndex;

	@Column(name = "payoff_ccy", length = 3)
	private String payoffCcy;

	@Column(name = "payoff_amount")
	private double payOffAmount;

	@Column(name = "notional_ccy", length = 3)
	private String notionalCcy;

	@Column(name = "notional")
	private double notional;

	@Column(name = "quantity")
	private double quantity;

	@Column(name = "tick_size")
	private int tickSize;

	@Column(name = "underlying_price")
	private double underlyingPrice;

	@Column(name = "strike")
	private double strike;

	@Column(name = "quanto_fx_rate")
	private double quantoFXRate;

	@Column(name = "pay_ccy", length = 3)
	private String payCCY;

	@Column(name = "rec_ccy", length = 3)
	private String recCCY;

	@Column(name = "pay_notional")
	private double payNotional;

	@Column(name = "rec_notional")
	private double recNotional;

	@Column(name = "sub_cls", length = 64)
	private String subCls;

	@Column(name = "underlying_product", length = 64)
	private String underlyingProduct;

	@Column(name = "settlement_type", length = 16)
	private String settlementType;

	@Column(name = "early_termination_date")
	private Date earlyTerminationDate;

	@Column(name = "option_type", length = 16)
	private String optionType;

	@Column(name = "strike2")
	private double strike2;
	
	@Column(name = "exercise_type", length = 16)
	private String exerciseType;
}
