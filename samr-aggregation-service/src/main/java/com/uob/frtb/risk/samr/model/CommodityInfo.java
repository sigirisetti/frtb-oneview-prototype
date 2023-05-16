package com.uob.frtb.risk.samr.model;

import com.uob.frtb.risk.common.model.BaseRiskEntity;

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
@Table(name = "frtb_samr_commodity_info")
public class CommodityInfo extends BaseRiskEntity {

	private static final long serialVersionUID = -6548076141244408984L;

	@Column(name = "trade_identifier", length = 32, nullable = false)
	protected String tradeIdentifier;

	@Column(name = "bucket")
	private Integer bucket;

	@Column(name = "product", length = 64)
	private String product;

	@Column(name = "underlying", length = 64)
	private String underlying;

	@Column(name = "grade", length = 12)
	private String grade;

	@Column(name = "delivery_location", length = 32)
	private String deliveryLocation;
}
