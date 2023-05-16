package com.uob.frtb.risk.samr.results;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "frtb_samr_po_results")
public class PoResults implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", length = 36, unique = true)
	private String id;

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "samr_results_id")
	private SAMRResults samrResults;

	@Column(name = "po", length = 32, nullable = false)
	private String po;
	
	@Column(name = "total_risk_charge")
	private double totalRiskCharge;

	@OneToMany(mappedBy = "poResults", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@OrderColumn(name = "hierarchy_order")
	private List<RiskClassHierarchyResultRow> hierarchyResults = new ArrayList<>();

	@OneToMany(mappedBy = "poResults", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@OrderColumn(name = "risk_class_results_order")
	private List<RiskClassLevelResults> riskClassResults = new ArrayList<>();
	
	@OneToMany(mappedBy = "poResults", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@OrderColumn(name = "inter_results_order")
	private List<IntermediateResultEntity> interResults;

	@Transient
	private String tradeIdentifier;

	public void addHierarchyResultRow(RiskClassHierarchyResultRow resultRow) {
		hierarchyResults.add(resultRow);
	}

	public void addRiskClassLevelResults(RiskClassLevelResults r) {
		if(!Double.isNaN(r.getTotalRiskCharge())) {
			riskClassResults.add(r);
			totalRiskCharge += r.getTotalRiskCharge();
		}
	}
}
