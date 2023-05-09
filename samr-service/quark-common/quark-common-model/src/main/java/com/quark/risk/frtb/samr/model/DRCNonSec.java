package com.quark.risk.frtb.samr.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "frtb_samr_drc_non_sec")
public class DRCNonSec extends BaseDRCEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "seniority", length = 16, nullable=false)
	private String seniority;

	@Column(name = "issuer_id", length = 64, nullable=false)
	private String issuerId;

	@Column(name = "issuer", length = 64, nullable=false)
	private String issuer;

	@Column
	private double strike;
}
