package com.quark.pricing.config;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class MarketDataItem implements Serializable {

	private static final long serialVersionUID = -8398894813596481936L;

	public enum Type {
		D, F, V
	}

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
	protected Long id;

	@Column(length = 3)
	protected String currency;

	@Column(name = "product_type", length = 32)
	protected String productType;

	@ManyToOne
	@JoinColumn(name = "market_data_set", referencedColumnName = "id", nullable = false)
	protected MarketDataSet marketDataSet;

	@Column
	protected String type;

	public MDIIdentifier getIdentifier() {
		return new MDIIdentifier(currency, productType);
	}
}
