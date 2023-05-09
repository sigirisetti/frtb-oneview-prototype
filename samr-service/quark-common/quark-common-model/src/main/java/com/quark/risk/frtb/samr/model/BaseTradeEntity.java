package com.quark.risk.frtb.samr.model;

import com.quark.risk.common.model.BaseRiskEntity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class BaseTradeEntity extends BaseRiskEntity {

	private static final long serialVersionUID = -7246236011453966568L;

	@Column(name = "trade_identifier", length = 32, nullable = false)
	protected String tradeIdentifier;

	@Column(name = "currency", length = 3, nullable = false)
	protected String currency;

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
		BaseTradeEntity other = (BaseTradeEntity) obj;
		if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }
}
