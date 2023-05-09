package com.quark.marketdata.quote;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Embeddable
public class QuoteKey implements Serializable {

	private static final long serialVersionUID = -4617174646701943169L;

	@Column(name = "quote_name", length = 128)
	private String quoteName;

	@Column(name = "quote_set_name", length = 16)
	private String quoteSetName;

	private Timestamp timestamp;
}
