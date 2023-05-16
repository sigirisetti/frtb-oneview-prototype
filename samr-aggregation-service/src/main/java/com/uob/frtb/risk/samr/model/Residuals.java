package com.uob.frtb.risk.samr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "frtb_samr_residuals")
public class Residuals extends BaseTradeEntity {

	private static final long serialVersionUID = -1275997944150902215L;

	@Column(name = "sensitivity_type", length = 16, nullable = false)
	@Enumerated(EnumType.STRING)
	protected RiskClassAndSensitivity sensitivityType;

	@Column(nullable=false)
	private double notional;

	@Column(nullable=false)
	private int type;
}
