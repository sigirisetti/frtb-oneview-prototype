package com.uob.frtb.risk.samr.model;

import com.uob.frtb.risk.common.model.BaseRiskEntity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "frtb_samr_equity_info")
public class EquityInfo extends BaseRiskEntity {

	private static final long serialVersionUID = 6264675145364622357L;

	@Column(name = "trade_identifier", length = 80)
	private String tradeIdentifier;

	@Column(name = "source", length = 16)
	private String source;

	@Column(name = "trade_id", length = 64)
	private String tradeId;

	@Column(name = "ccy", length = 3)
	private String currency;

	@Column(name = "bucket")
	private Integer bucket;

	@Column(name = "product", length = 36)
	private String product;

	@Column(name = "issuer", length = 36)
	private String issuer;

	@Column
	@Type(type = "yes_no")
	private Boolean repo;

	@Column(name = "market_cap", length = 16)
	private String marketCap;

	@Column(name = "economy", length = 36)
	private String economy;

	@Column(name = "sector", length = 64)
	private String sector;

	public void setTradeIdentifier(String tradeIdentifier) {
		this.tradeIdentifier = tradeIdentifier;
		if (StringUtils.isBlank(tradeIdentifier)) {
			throw new IllegalArgumentException("Trade identifier is blank");
		}
		String s[] = tradeIdentifier.split("_");
		if (s.length != 2) {
			throw new IllegalArgumentException("Expecting trade identifier pattern <Source>_<NumericId>");
		}
		setSource(s[0]);
		setTradeId(s[1]);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		EquityInfo other = (EquityInfo) obj;
		if (id == null) {
			return other.id == null;
		} else return id.equals(other.id);
	}
}
