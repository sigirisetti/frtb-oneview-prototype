package com.quark.risk.frtb.samr.model;

import com.quark.risk.common.model.BaseRiskEntity;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "frtb_samr_hierarchy")
public class Hierarchy extends BaseRiskEntity {

	private static final long serialVersionUID = -7147572163321539868L;

	@Column(name = "trade_identifier", length = 80)
	private String tradeIdentifier;

	@Column(name = "source", length = 16)
	private String source;

	@Column(name = "trade_id", length = 64)
	private String tradeId;

	@Column(name = "po", length = 16)
	private String po;

	@Column(name = "product", length = 32)
	private String product;

	@Column(name = "desk", length = 16)
	private String desk;

	@Column(name = "book", length = 16)
	private String book;

	@Column(name = "location", length = 16)
	private String location;

	public void setTradeIdentifier(String tradeIdentifier) {
		this.tradeIdentifier = tradeIdentifier;
		if (StringUtils.isBlank(tradeIdentifier)) {
			throw new IllegalArgumentException("Trade identifier is blank");
		}
		String[] s = tradeIdentifier.split("_");
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
		Hierarchy other = (Hierarchy) obj;
		if (id == null) {
			return other.id == null;
		} else return id.equals(other.id);
	}
}
