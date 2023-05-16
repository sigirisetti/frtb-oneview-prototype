package com.uob.frtb.ui.refdata;

import com.uob.frtb.pricing.config.PricingContextConfig;

import java.io.Serializable;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PricingContextConfigWindowRefData extends WindowRefData implements Serializable {

	private static final long serialVersionUID = -3676351604410560793L;

	private Set<String> pricingEngineConfigNames;
	private Set<String> quoteSetNames;
	private Set<PricingContextConfig> pricingContext;
}
