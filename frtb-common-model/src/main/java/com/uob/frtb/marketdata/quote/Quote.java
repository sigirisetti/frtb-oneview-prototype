package com.uob.frtb.marketdata.quote;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "quote")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "quote")
public class Quote implements Serializable, Comparable<Quote> {
	private static final long serialVersionUID = -934635300975858672L;

	@EmbeddedId
	private QuoteKey key;

	@Version
	@Column
	protected Long version;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "quote_name", insertable = false, updatable = false)
	private QuoteName quoteName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quote_set_name", insertable = false, updatable = false)
	private QuoteSet quoteSet;

	private Double open;
	private Double close;
	private Double bid;
	private Double ask;

	public Quote() {
	}

	public Quote(QuoteName quoteName, Timestamp ts, QuoteSet quoteSet, Double open, Double close, Double bid,
			Double ask) {
		this.key = new QuoteKey();
		key.setQuoteName(quoteName.getQuoteName());
		key.setTimestamp(ts);
		if (quoteSet != null) {
			key.setQuoteSetName(quoteSet.getQuoteSetName());
		}
		this.quoteName = quoteName;
		this.quoteSet = quoteSet;
		this.open = open;
		this.close = close;
		this.bid = bid;
		this.ask = ask;
	}

	@Override
	public int compareTo(Quote o) {
		return this.getKey().getTimestamp().compareTo(o.getKey().getTimestamp());
	}

	public boolean hasValues() {
		return open != null || close != null || bid != null || ask != null;
	}

	public boolean hasAllValues() {
		return open != null || close != null || bid != null || ask != null;
	}
}
