package com.uob.frtb.risk.frtb.samr.model;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class BaseDRCEntity extends BaseTradeEntity {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "sensitivity_type", length = 16, nullable = false)
	@Enumerated(EnumType.STRING)
	protected RiskClassAndSensitivity sensitivityType;

	@Column(name = "bucket", nullable=false)
	private Integer bucket;

	@Column(name = "product", length = 64, nullable=false)
	private String product;

	@Column(name = "rating", length = 8, nullable=false)
	private String rating;

	@Column(name = "long_short", length = 8, nullable=false)
	private String longOrShort;

	@Column(nullable=false)
	private double maturity;

	@Column(nullable=false)
	private double notional;

	@Column(nullable=false)
	private double mtm;

}
