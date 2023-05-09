package com.quark.pricing.config;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MDIIdentifier implements Serializable {

	private static final long serialVersionUID = -3232909497670317923L;

	protected String currency;
	protected String productType;

	public MDIIdentifier(String currency) {
		this.currency = currency;
	}
}
