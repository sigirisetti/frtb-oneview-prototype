package com.uob.frtb.risk.samr.model;

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
@Table(name = "frtb_samr_credit_issuer_info")
public class CreditIssuerInfo extends BaseTradeEntity {

	private static final long serialVersionUID = 6622718119148264491L;

	@Column(name = "bucket")
	private Integer bucket;

	@Column(name = "issuer_id", length = 64)
	private String issuerId;

	@Column(name = "issuer", length = 64)
	private String issuer;

	@Column(name = "bond_or_cds", length = 6)
	private String bondOrCds;

	@Column(name = "rating", length = 16)
	private String rating;

	@Column(name = "sector", length = 64)
	private String sector;
}
