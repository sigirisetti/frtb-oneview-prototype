package com.quark.marketdata.curve;

import com.quark.marketdata.quote.QuoteName;
import com.quark.refdata.model.Tenor;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "curve_underlying_fra")
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "cu_id_seq")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "curveunderlying")
public class CurveUnderlyingFRA extends CurveUnderlying implements Serializable {

	private static final long serialVersionUID = -4581086342336438334L;

	@Column(name = "ibor_index", length = 16, nullable = false)
	private String iborIndex;

	@Column(name = "provider", length = 16, nullable = false)
	private String provider;

	@Column(name = "fixing_days", length = 16, nullable = false)
	private Integer fixingDays;

	@Column(nullable = false)
	private Boolean eom;

	@Transient
	protected Tenor term;

	@Column(name = "term", length = 6, nullable = false)
	protected String termAsString;

	public CurveUnderlyingFRA() {
	}

	public CurveUnderlyingFRA(String ccy, String quoteType, String term, String tenor, String calendar,
			String businessDayConvention, String dayCounter, int fixingDays, boolean eom, String provider) {
		super(ccy, quoteType, tenor, calendar, businessDayConvention, dayCounter);
		this.fixingDays = fixingDays;
		this.eom = eom;
		this.termAsString = term;
		this.provider = provider;
	}

	@Override
	public String getType() {
		return CurveUnderlyingType.FRA.type();
	}

	@Override
	public String getDescription() {
		return String.format("%s.%s.%s.%s/%s", getType(), getCurrency(), getIborIndex(), getTermAsString(),
				getTenorAsString(), getProvider());
	}

	@Override
	public String getQuoteNameAsString() {
		return String.format("%s.%s.%s.%s/%s", getType(), getCurrency(), getIborIndex(), getTenorAsString(),
				getProvider());
	}

	@Override
	protected QuoteName getQuoteName() {
		return new QuoteName(getQuoteNameAsString(), quoteType);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((eom == null) ? 0 : eom.hashCode());
		result = prime * result + ((fixingDays == null) ? 0 : fixingDays.hashCode());
		result = prime * result + ((iborIndex == null) ? 0 : iborIndex.hashCode());
		result = prime * result + ((term == null) ? 0 : term.hashCode());
		result = prime * result + ((termAsString == null) ? 0 : termAsString.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CurveUnderlyingFRA other = (CurveUnderlyingFRA) obj;
		if (eom == null) {
			if (other.eom != null)
				return false;
		} else if (!eom.equals(other.eom))
			return false;
		if (fixingDays == null) {
			if (other.fixingDays != null)
				return false;
		} else if (!fixingDays.equals(other.fixingDays))
			return false;
		if (iborIndex == null) {
			if (other.iborIndex != null)
				return false;
		} else if (!iborIndex.equals(other.iborIndex))
			return false;
		if (term == null) {
			if (other.term != null)
				return false;
		} else if (!term.equals(other.term))
			return false;
		if (termAsString == null) {
            return other.termAsString == null;
        } else return termAsString.equals(other.termAsString);
    }
}
