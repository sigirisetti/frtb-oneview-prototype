package com.uob.frtb.risk.frtb.samr.model;

import com.uob.frtb.risk.common.model.BaseRiskEntity;
import com.uob.frtb.risk.common.model.WorkflowInstance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "frtb_samr_market_quote")
public class SamrMarketQuote extends BaseRiskEntity {

	private static final long serialVersionUID = 821069238988029284L;

	@Column(name = "quote_name", length = 36)
	private String quoteName;

	@Column(name = "quote_value")
	private double quoteValue;

	public SamrMarketQuote(WorkflowInstance workflowInstance, String quoteName, double quoteValue) {
		this.workflowInstance = workflowInstance;
		this.quoteName = quoteName;
		this.quoteValue = quoteValue;
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
		SamrMarketQuote other = (SamrMarketQuote) obj;
		if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }
}
