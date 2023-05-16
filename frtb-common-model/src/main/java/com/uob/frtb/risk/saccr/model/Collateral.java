package com.uob.frtb.risk.saccr.model;

import com.uob.frtb.risk.common.model.BaseRiskEntity;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Table(name = "saccr_collateral")
public class Collateral extends BaseRiskEntity {

	private static final long serialVersionUID = -8205821317012572699L;

	@Column(name = "key_id", length = 64, nullable = false)
	private String key;

	@Column(name = "counterparty", length = 64, nullable = false)
	private String counterparty;

	@Column(name = "la_id", length = 64)
	private String laId;

	@Column(name = "csa_id", length = 64)
	private String csaId;

	@Column(name = "collateral_balance")
	private double collateralBalance;

	@Column(name = "haircut")
	private double haircut;

	@Column(name = "th_po")
	private double thresholdForPo;

	@Column(name = "th_cpty")
	private double thresholdForCounterparty;

	@Column(name = "mta_po")
	private double mtaForPo;

	@Column(name = "mta_cpty")
	private double mtaForCounterparty;

	@Column(name = "ia_po")
	private double iaForPo;

	@Column(name = "ia_cpty")
	private double iaForCounterparty;

	@Column(name = "nica")
	private double nica;

	@Column(name = "col_call_freq")
	private int collateralCallFrequency;

	@Column
	@Type(type = "yes_no")
	private Boolean dispute;

	@Column
	@Type(type = "yes_no")
	private Boolean liquid;

	@Column
	@Type(type = "yes_no")
	private Boolean clearing;
}
