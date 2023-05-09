package com.quark.pricing.config;

import com.quark.marketdata.curve.Curve;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "pricing_engine_config_item")
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "pe_config_item_id_seq")
public class PricingEngineConfigItem implements Serializable {

	private static final long serialVersionUID = -7704154411405158942L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
	private Long id;

	@Column(name = "pricing_engine_name", length = 32, nullable = false)
	private String pricingEngineName;

	@Column(name = "product_type", length = 32, nullable = false)
	private String productType;

	@Column(length = 3, nullable = false)
	private String currency;

	@ManyToOne
	@JoinColumn(name = "discount_curve_id", nullable = false)
	private Curve discountCurve;

	@ManyToOne
	@JoinColumn(name = "pricing_engine_config_id", referencedColumnName = "id", nullable = false)
	private PricingEngineConfig pricingEngineConfig;

	public PricingEngineConfigItem(String pricingEngineName, String productType, String currency, Curve discountCurve) {
		this.pricingEngineName = pricingEngineName;
		this.productType = productType;
		this.currency = currency;
		this.discountCurve = discountCurve;
	}
}
