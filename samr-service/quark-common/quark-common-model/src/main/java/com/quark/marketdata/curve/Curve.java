package com.quark.marketdata.curve;

import com.quark.marketdata.quote.Quote;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "curve_def", uniqueConstraints = @UniqueConstraint(columnNames = { "currency", "rate_index",
		"rate_index_tenor" }))
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "curve_def_id_seq")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "curve")
public class Curve implements Serializable {

	private static final long serialVersionUID = -299192666383559801L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
	private Long id;

	@Column(length = 64)
	private String name;

	@Column(name = "curve_timestamp", updatable = false)
	private Timestamp timestamp;

	@Column(length = 8)
	private String instance;

	@Version
	@Column
	protected Long version;

	@Column(length = 3, nullable = false)
	private String currency;

	@Column(name = "rate_index", length = 32, nullable = false)
	private String rateIndex;

	@Column(name = "rate_index_tenor", length = 16, nullable = false)
	private String rateIndexTenor;

	@Column(name = "calendar", length = 16, nullable = false)
	private String calendar;

	@Column(name = "interpolator", length = 32, nullable = false)
	private String interpolator;

	@Column(name = "pricing_context", length = 32, nullable = false)
	private String pricingContext;

	@Column(name = "day_counter", length = 32, nullable = false)
	protected String dayCounter;

	@Transient
	private List<CurveUnderlyingMap> underlyingMap;

	@Transient
	private List<Quote> quotes;

	@Override
	public String toString() {
		return getCurrency().concat("/").concat(getRateIndex()).concat("/").concat(getRateIndexTenor()).concat("/")
				.concat(getName());
	}
}
