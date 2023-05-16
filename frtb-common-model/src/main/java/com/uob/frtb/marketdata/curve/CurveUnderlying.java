package com.uob.frtb.marketdata.curve;

import com.uob.frtb.marketdata.quote.Quote;
import com.uob.frtb.marketdata.quote.QuoteName;
import com.uob.frtb.refdata.model.Tenor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.io.Serializable;

@MappedSuperclass
public class CurveUnderlying implements Serializable {

	private static final long serialVersionUID = 665463414301768494L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
	protected Long id;

	@Version
	@Column
	protected Long version;

	@Column(length = 3, nullable = false)
	protected String currency;

	@Transient
	protected String quoteName = "";

	@Column(name = "quote_type", length = 16, nullable = false)
	protected String quoteType;

	@Transient
	protected Tenor tenor;

	@Column(name = "tenor", length = 6, nullable = false)
	protected String tenorAsString;

	@Transient
	protected Quote quote;

	@Column(length = 32, nullable = false)
	protected String calendar;

	@Column(name = "date_convention", length = 32, nullable = false)
	protected String dateConvention;

	@Column(name = "day_counter", length = 32, nullable = false)
	protected String dayCounter;

	public CurveUnderlying() {
	}

	public CurveUnderlying(String ccy, String quoteType, String tenor, String calendar, String dateConvention,
			String dayCounter) {
		this.currency = ccy;
		this.quoteType = quoteType;
		this.setTenorAsString(tenor);
		this.calendar = calendar;
		this.dateConvention = dateConvention;
		this.dayCounter = dayCounter;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public void setQuoteName(String quoteName) {
		this.quoteName = quoteName;
	}

	public String getQuoteType() {
		return quoteType;
	}

	public void setQuoteType(String quoteType) {
		this.quoteType = quoteType;
	}

	public Tenor getTenor() {
		if (tenor != null) {
			return tenor;
		} else if (StringUtils.isNotBlank(tenorAsString)) {
			tenor = Tenor.parseTenor(tenorAsString);
			return tenor;
		}
		return null;
	}

	public void setTenor(Tenor tenor) {
		this.tenor = tenor;
		this.tenorAsString = tenor.toString();
	}

	public String getTenorAsString() {
		return tenorAsString;
	}

	public void setTenorAsString(String tnr) {
		this.tenorAsString = tnr;
		this.tenor = Tenor.parseTenor(tnr);
	}

	public void setQuote(Quote quote) {
		this.quote = quote;
	}

	public String getCalendar() {
		return calendar;
	}

	public void setCalendar(String calendar) {
		this.calendar = calendar;
	}

	public String getDateConvention() {
		return dateConvention;
	}

	public void setDateConvention(String businessDayConvention) {
		this.dateConvention = businessDayConvention;
	}

	public String getDayCounter() {
		return dayCounter;
	}

	public void setDayCounter(String dayCounter) {
		this.dayCounter = dayCounter;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	// -------- Helper methods

	public Quote getQuote() {
		if (quote == null) {
			quote = new Quote(getQuoteName(), null, null, null, null, null, null);
		}
		return quote;
	}

	protected QuoteName getQuoteName() {
		return new QuoteName(getQuoteNameAsString(), getQuoteType());
	}

	public String getType() {
		return "";
	}

	public String getDescription() {
		return "";
	}

	public String getQuoteNameAsString() {
		return quoteName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((calendar == null) ? 0 : calendar.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((dateConvention == null) ? 0 : dateConvention.hashCode());
		result = prime * result + ((dayCounter == null) ? 0 : dayCounter.hashCode());
		result = prime * result + ((quoteType == null) ? 0 : quoteType.hashCode());
		result = prime * result + ((tenorAsString == null) ? 0 : tenorAsString.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CurveUnderlying other = (CurveUnderlying) obj;
		if (calendar == null) {
			if (other.calendar != null)
				return false;
		} else if (!calendar.equals(other.calendar))
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (dateConvention == null) {
			if (other.dateConvention != null)
				return false;
		} else if (!dateConvention.equals(other.dateConvention))
			return false;
		if (dayCounter == null) {
			if (other.dayCounter != null)
				return false;
		} else if (!dayCounter.equals(other.dayCounter))
			return false;
		if (quoteType == null) {
			if (other.quoteType != null)
				return false;
		} else if (!quoteType.equals(other.quoteType))
			return false;
		if (tenorAsString == null) {
			if (other.tenorAsString != null)
				return false;
		} else if (!tenorAsString.equals(other.tenorAsString))
			return false;
		return true;
	}

}
