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
@Table(name = "frtb_samr_drc_sec_ctp")
public class DRCSecCTP extends BaseDRCEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "credit_index", length = 64, nullable = false)
	private String index;

	@Column(name = "region", length = 16, nullable = false)
	private String region;

	@Column(name = "series", nullable = false)
	private Integer series;

	@Column(name = "securitization", nullable = false)
	private Boolean securitization;
}
