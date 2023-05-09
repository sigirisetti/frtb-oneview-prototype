package com.quark.marketdata.curve;

import com.quark.marketdata.quote.QuoteName;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "curve_underlying_future")
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "cu_id_seq")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "curveunderlying")
public class CurveUnderlyingFuture extends CurveUnderlying implements Serializable {

	private static final long serialVersionUID = -3530492305067822851L;

	@Column(name = "contract_name", nullable = false)
	private String contractName;

	@Column(name = "exchange", nullable = false)
	private String exchange;

	@Column(name = "imm_date", nullable = true)
	private Date immDate;

	@Column(nullable = true)
	private Boolean eom;

	@Column(name = "convexity_adjustment", nullable = true)
	private Double convexityAdjustment;

	public CurveUnderlyingFuture() {
	}

	public CurveUnderlyingFuture(String contractName, String exchange, String ccy, String quoteType, String tenor,
			String calendar, String businessDayConvention, String dayCounter, boolean eom, Date immDate,
			Double convexityAdj) {
		super(ccy, quoteType, tenor, calendar, businessDayConvention, dayCounter);
		this.contractName = contractName;
		this.exchange = exchange;
		this.immDate = immDate;
		this.eom = eom;
		this.convexityAdjustment = convexityAdj;
	}

	@Override
	public String getType() {
		return CurveUnderlyingType.FUTURE.type();
	}

	@Override
	public String getDescription() {
		return String.format("%s.%s.%s.%s.%s", getType(), getCurrency(), getExchange(), getContractName(),
				getTenorAsString());
	}

	@Override
	public String getQuoteNameAsString() {
		return String.format("%s.%s.%s.%s.%s", getType(), getCurrency(), getExchange(), getContractName(),
				getTenorAsString());
	}

	@Override
	protected QuoteName getQuoteName() {
		return new QuoteName(getQuoteNameAsString(), quoteType);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((contractName == null) ? 0 : contractName.hashCode());
		result = prime * result + ((convexityAdjustment == null) ? 0 : convexityAdjustment.hashCode());
		result = prime * result + ((eom == null) ? 0 : eom.hashCode());
		result = prime * result + ((exchange == null) ? 0 : exchange.hashCode());
		result = prime * result + ((immDate == null) ? 0 : immDate.hashCode());
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
		CurveUnderlyingFuture other = (CurveUnderlyingFuture) obj;
		if (contractName == null) {
			if (other.contractName != null)
				return false;
		} else if (!contractName.equals(other.contractName))
			return false;
		if (convexityAdjustment == null) {
			if (other.convexityAdjustment != null)
				return false;
		} else if (!convexityAdjustment.equals(other.convexityAdjustment))
			return false;
		if (eom == null) {
			if (other.eom != null)
				return false;
		} else if (!eom.equals(other.eom))
			return false;
		if (exchange == null) {
			if (other.exchange != null)
				return false;
		} else if (!exchange.equals(other.exchange))
			return false;
		if (immDate == null) {
            return other.immDate == null;
        } else return immDate.equals(other.immDate);
    }
}
