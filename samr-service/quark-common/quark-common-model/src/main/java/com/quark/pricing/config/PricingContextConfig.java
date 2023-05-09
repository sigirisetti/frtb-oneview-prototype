package com.quark.pricing.config;

import com.quark.marketdata.quote.QuoteSet;

import java.io.Serializable;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pricing_context_config")
public class PricingContextConfig implements Serializable, Comparable<PricingContextConfig> {

	private static final long serialVersionUID = -1059551164017851360L;

	public static final String DEFAULT_PRICING_CONTEXT_CONFIG = "default";
	public static final String DEFAULT_PRICING_ENGINE_CONFIG = "default";
	public static final String DEFAULT_QUOTE_SET_NAME = "default";

	@Id
	@Column(length = 32)
	private String name;

	@Column(name = "time_zone", length = 16)
	private TimeZone timeZone;

	@Column(name = "currency", length = 3)
	private String currency;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "quote_set")
	private QuoteSet quoteSet;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pricing_engine_config")
	private PricingEngineConfig pricingEngineConfig;

	public PricingContextConfig(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(PricingContextConfig o) {
		return getName().compareTo(o.getName());
	}
}
