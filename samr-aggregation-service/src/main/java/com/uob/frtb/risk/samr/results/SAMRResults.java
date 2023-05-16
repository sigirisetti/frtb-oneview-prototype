package com.uob.frtb.risk.samr.results;

import com.uob.frtb.risk.common.model.WorkflowInstance;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "frtb_samr_results")
public class SAMRResults implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", length = 36, unique = true)
	private String id;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "workflow_id")
	private WorkflowInstance workflowInstance;

	@Column(name = "currency", length = 3, nullable = false)
	private String currency;

	@Column(name = "total_risk_charge")
	private double totalRiskCharge;

	@OneToMany(mappedBy = "samrResults", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@OrderColumn(name = "po_order")
	private List<PoResults> poResults = new ArrayList<>();

	@Transient
	private String tradeIdentifier;

	public SAMRResults(WorkflowInstance workflowInstance, String currency) {
		this.workflowInstance = workflowInstance;
		this.currency = currency;
	}

	public void addPoResults(PoResults r) {
		if (!Double.isNaN(r.getTotalRiskCharge())) {
			poResults.add(r);
			totalRiskCharge += r.getTotalRiskCharge();
		}
	}

	public List<RiskClassLevelResults> getRiskClassResults() {
		Map<String, RiskClassLevelResults> riskClassLevelResults = new LinkedHashMap<>();
		for(PoResults r : poResults) {
			List<RiskClassLevelResults> rclResultList = r.getRiskClassResults();
			for(RiskClassLevelResults rcl : rclResultList) {
				if(riskClassLevelResults.containsKey(rcl.getRiskClass().toString())) {
					riskClassLevelResults.get(rcl.getRiskClass().toString()).aggregate(rcl);
				}else {
					riskClassLevelResults.put(rcl.getRiskClass().toString(), rcl);
				}
			}
		}
		return new ArrayList<>(riskClassLevelResults.values());
	}

}
