package com.quark.marketdata.curve;

import com.quark.marketdata.quote.QuoteName;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "curve_underlying_money_market")
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "cu_id_seq")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="curveunderlying")
public class CurveUnderlyingMoneyMarket extends CurveUnderlying {

	private static final long serialVersionUID = 2541618072297583099L;

	@Column(name = "ibor_index", length = 16, nullable = false)
	private String iborIndex;

	@Column(name = "fixing_days", length = 16, nullable = false)
	private Integer fixingDays;

	@Column(nullable = false)
	private Boolean eom;

	@Column(name = "provider", length = 16, nullable = false)
	private String provider;

	public CurveUnderlyingMoneyMarket() {
	}

	public CurveUnderlyingMoneyMarket(String index, String ccy, String quoteType, String tenor, String calendar,
			String businessDayConvention, String dayCounter, int fixingDays, boolean eom, String provider) {
		super(ccy, quoteType, tenor, calendar, businessDayConvention, dayCounter);
		this.iborIndex = index;
		this.fixingDays = fixingDays;
		this.eom = eom;
		this.provider = provider;
	}


	@Override
	public String getType() {
		return CurveUnderlyingType.MONEY_MARKET.type();
	}

	@Override
	public String getDescription() {
		return String.format("%s.%s.%s.%s/%s", getType(), getCurrency(), getIborIndex(), getTenorAsString(),
				getProvider());
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
		result = prime * result + ((provider == null) ? 0 : provider.hashCode());
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
		CurveUnderlyingMoneyMarket other = (CurveUnderlyingMoneyMarket) obj;
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
		if (provider == null) {
            return other.provider == null;
        } else return provider.equals(other.provider);
    }

}
