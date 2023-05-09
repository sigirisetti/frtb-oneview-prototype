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
@Table(name = "curve_underlying_swap")
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "cu_id_seq")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "curveunderlying")
public class CurveUnderlyingSwap extends CurveUnderlying {

	private static final long serialVersionUID = -4303195284852616660L;

	@Column(name = "fixed_leg_frequency", length = 16, nullable = false)
	private String fixedLegFrequency;

	@Column(name = "float_leg_frequency", length = 16, nullable = false)
	private String floatLegFrequency;

	@Column(name = "float_leg_index", length = 16, nullable = false)
	private String floatLegIndex;

	@Column(name = "provider", length = 32, nullable = false)
	private String provider;

	public CurveUnderlyingSwap() {
	}

	public CurveUnderlyingSwap(String ccy, String quoteType, String indexTenor, String calendar,
			String businessDayConvention, String dayCounter, String fixedLegFrequency, String floatLegFrequency,
			String floatLegIndex, String provider) {
		super(ccy, quoteType, indexTenor, calendar, businessDayConvention, dayCounter);
		this.fixedLegFrequency = fixedLegFrequency;
		this.floatLegFrequency = floatLegFrequency;
		this.floatLegIndex = floatLegIndex;
		this.provider = provider;
	}

	public String getType() {
		return CurveUnderlyingType.SWAP.type();
	}

	@Override
	public String getDescription() {
		return String.format("%s.%s.%s/%s/%s.%s/%s", getType(), getTenor(), getCurrency(), getFixedLegFrequency(),
				getFloatLegIndex(), getFloatLegFrequency(), getProvider());
	}

	@Override
	public String getQuoteNameAsString() {
		return String.format("%s.%s.%s/%s/%s.%s/%s", getType(), getTenor(), getCurrency(), getFixedLegFrequency(),
				getFloatLegIndex(), getFloatLegFrequency(), getProvider());
	}

	@Override
	protected QuoteName getQuoteName() {
		return new QuoteName(getQuoteNameAsString(), quoteType);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((fixedLegFrequency == null) ? 0 : fixedLegFrequency.hashCode());
		result = prime * result + ((floatLegFrequency == null) ? 0 : floatLegFrequency.hashCode());
		result = prime * result + ((floatLegIndex == null) ? 0 : floatLegIndex.hashCode());
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
		CurveUnderlyingSwap other = (CurveUnderlyingSwap) obj;
		if (fixedLegFrequency == null) {
			if (other.fixedLegFrequency != null)
				return false;
		} else if (!fixedLegFrequency.equals(other.fixedLegFrequency))
			return false;
		if (floatLegFrequency == null) {
			if (other.floatLegFrequency != null)
				return false;
		} else if (!floatLegFrequency.equals(other.floatLegFrequency))
			return false;
		if (floatLegIndex == null) {
			if (other.floatLegIndex != null)
				return false;
		} else if (!floatLegIndex.equals(other.floatLegIndex))
			return false;
		if (provider == null) {
            return other.provider == null;
        } else return provider.equals(other.provider);
    }

}
