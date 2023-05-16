package com.uob.frtb.risk.frtb.samr.model;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "frtb_samr_eq_classification")
public class EquityClassification implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", length = 36, unique = true)
	private String id;

	@Column(name = "sector", length = 128)
	private String sector;

	@Column(name = "region", length = 32)
	private String region;

	@Column(name = "market_cap", length = 16)
	private String marketCap;

	@Column(name = "bucket")
	private Integer bucket;

	public EquityClassification(String sector, String region, String marketCap, Integer bucket) {
		this.sector = sector;
		this.region = region;
		this.marketCap = marketCap;
		this.bucket = bucket;
	}
}
