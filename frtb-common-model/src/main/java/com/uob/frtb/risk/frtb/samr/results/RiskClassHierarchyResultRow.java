package com.uob.frtb.risk.frtb.samr.results;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "frtb_samr_h_result_rows")
public class RiskClassHierarchyResultRow implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", length = 36, unique = true)
	@JsonIgnore
	private String id;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "po_results_id")
	private PoResults poResults;

	@Column(name = "parent_key", length = 64)
	private String parentKey;

	@Column(length = 12)
	private String currency;

	@Column(name = "base_currency", length = 12)
	private String baseCurrency;

	@Column(name = "result_name", length = 16)
	private String resultName;

	@Column(name = "amount_low_corr")
	private double amountLowCorr;

	@Column(name = "amount_base_low_corr")
	private double amountBaseLowCorr;

	@Column(name = "amount")
	private double amount;

	@Column(name = "amount_base")
	private double amountBase;

	@Column(name = "amount_high_corr")
	private double amountHighCorr;

	@Column(name = "amount_base_high_corr")
	private double amountBaseHighCorr;

	public RiskClassHierarchyResultRow(PoResults poResults, String currency, String baseCurrency, String resultName,
			String parentKey, double amount, double amountBase, double amountLowCorr, double amountBaseLowCorr,
			double amountHighCorr, double amountBaseHighCorr) {
		this.poResults = poResults;
		this.currency = currency;
		this.baseCurrency = baseCurrency;
		this.resultName = resultName;
		this.parentKey = parentKey;
		this.amount = amount;
		this.amountBase = amountBase;
		this.amountLowCorr = amountLowCorr;
		this.amountBaseLowCorr = amountBaseLowCorr;
		this.amountHighCorr = amountHighCorr;
		this.amountBaseHighCorr = amountBaseHighCorr;
	}

	public String buildParentKey() {
		return parentKey.concat(":").concat(resultName);
	}
}
