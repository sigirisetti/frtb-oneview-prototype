package com.uob.frtb.risk.frtb.samr.model;

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
@Table(name = "frtb_samr_drc_sec_non_ctp")
public class DRCSecNonCTP extends BaseDRCEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "seniority", length = 16, nullable=false)
	private String seniority;

	@Column(name = "portfolio", length = 64, nullable=false)
	private String portfolio;

	@Column(name = "bond_or_cds", length = 12, nullable=false)
	private String bondOrCds;

	@Column(name = "category", length = 32, nullable=false)
	private String category;

	@Column(name = "region", length = 16, nullable=false)
	private String region;
}
