package com.uob.frtb.risk.saccr.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "saccr_req_trade_attrs")
public class RequiredTradeAttributes implements Serializable {

	private static final long serialVersionUID = -823477314657182715L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", length = 36, unique = true)
	protected String id;

	@Column(name = "product_name", length = 64, nullable = false)
	private String productName;

	@Column(name = "asset_class", length = 16, nullable = false)
	private String assetClass;

	@Column(name = "description", length = 256)
	private String description;

	@Column(name = "product")
	@Type(type = "yes_no")
	private boolean product;

	@Column(name = "legal_entity")
	@Type(type = "yes_no")
	private boolean legalEntity;

	@Column(name = "la_name")
	@Type(type = "yes_no")
	private boolean laName;

	@Column(name = "csa_name")
	@Type(type = "yes_no")
	private boolean csaName;

	@Column(name = "po")
	@Type(type = "yes_no")
	private boolean po;

	@Column(name = "trade_id")
	@Type(type = "yes_no")
	private boolean tradeId;

	@Column(name = "source_system")
	@Type(type = "yes_no")
	private boolean sourceSystem;

	@Column(name = "otc_type")
	@Type(type = "yes_no")
	private boolean otcType;

	@Column(name = "npv_ccy")
	@Type(type = "yes_no")
	private boolean npvCcy;

	@Column(name = "npv")
	@Type(type = "yes_no")
	private boolean npv;

	@Column(name = "start_date")
	@Type(type = "yes_no")
	private boolean startDate;

	@Column(name = "end_date")
	@Type(type = "yes_no")
	private boolean endDate;

	@Column(name = "next_exercise_date")
	@Type(type = "yes_no")
	private boolean nextExerciseDate;

	@Column(name = "buy_sell")
	@Type(type = "yes_no")
	private boolean buySell;

	@Column(name = "put_call")
	@Type(type = "yes_no")
	private boolean putCall;

	@Column(name = "pay_ref_idx")
	@Type(type = "yes_no")
	private boolean payRefIndex;

	@Column(name = "rec_ref_idx")
	@Type(type = "yes_no")
	private boolean recRefIndex;

	@Column(name = "payoff_ccy")
	@Type(type = "yes_no")
	private boolean payoffCcy;

	@Column(name = "payoff_amount")
	@Type(type = "yes_no")
	private boolean payOffAmount;

	@Column(name = "notional_ccy")
	@Type(type = "yes_no")
	private boolean notionalCcy;

	@Column(name = "notional")
	@Type(type = "yes_no")
	private boolean notional;

	@Column(name = "quantity")
	@Type(type = "yes_no")
	private boolean quantity;

	@Column(name = "tick_size")
	@Type(type = "yes_no")
	private boolean tickSize;

	@Column(name = "underlying_price")
	@Type(type = "yes_no")
	private boolean underlyingPrice;

	@Column(name = "strike")
	@Type(type = "yes_no")
	private boolean strike;

	@Column(name = "quanto_fx_rate")
	@Type(type = "yes_no")
	private boolean quantoFXRate;

	@Column(name = "pay_ccy")
	@Type(type = "yes_no")
	private boolean payCcy;

	@Column(name = "rec_ccy")
	@Type(type = "yes_no")
	private boolean recCcy;

	@Column(name = "pay_notional")
	@Type(type = "yes_no")
	private boolean payNotional;

	@Column(name = "rec_notional")
	@Type(type = "yes_no")
	private boolean recNotional;

	@Column(name = "sub_cls")
	@Type(type = "yes_no")
	private boolean subCls;

	@Column(name = "underlying_product")
	@Type(type = "yes_no")
	private boolean underlyingProduct;

	@Column(name = "settlement_type")
	@Type(type = "yes_no")
	private boolean settlementType;

	@Column(name = "early_termination_date")
	@Type(type = "yes_no")
	private boolean earlyTerminationDate;

	@Column(name = "option_type")
	@Type(type = "yes_no")
	private boolean optionType;

	@Column(name = "strike2")
	@Type(type = "yes_no")
	private boolean strike2;
}
