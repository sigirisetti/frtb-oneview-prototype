package com.uob.frtb.marketdata.quote;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "quote_set")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "quote")
public class QuoteSet implements Serializable {

	private static final long serialVersionUID = 7745850514460444431L;

	@Id
	@Column(name = "quote_set_name", length = 16)
	private String quoteSetName;

	@Column(length = 128)
	private String description;

	@Column(length = 16)
	private String parent;

	@Transient
	private Set<Quote> quotes;

	@Transient
	private Map<String, TreeSet<Quote>> quoteMap;

	public QuoteSet(String name, String description) {
		this.quoteSetName = name;
		this.description = description;
	}

	public void setQuotes(Set<Quote> quotes) {
		this.quotes = quotes;
		this.quoteMap = new HashMap<>();
		for (Quote q : quotes) {
			TreeSet<Quote> set = quoteMap.get(q.getQuoteName().getQuoteName());
			if (set == null) {
				set = new TreeSet<>();
				quoteMap.put(q.getQuoteName().getQuoteName(), set);
			}
			set.add(q);
		}
	}

	public Quote getLatestQuote(String qn) {
		TreeSet<Quote> set = quoteMap.get(qn);
		return set == null ? null : set.last();
	}

	public Quote getQuote(String qn) {
		TreeSet<Quote> set = quoteMap.get(qn);
		return set == null ? null : set.last();
	}
}
