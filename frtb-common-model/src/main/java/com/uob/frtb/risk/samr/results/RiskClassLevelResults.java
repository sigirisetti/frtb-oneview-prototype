package com.uob.frtb.risk.samr.results;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uob.frtb.risk.samr.model.RiskClass;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString(exclude = "poResults")
@Table(name = "frtb_samr_risk_class_results")
public class RiskClassLevelResults implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", length = 36, unique = true)
	private String id;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "po_results_id")
	private PoResults poResults;

	@Column(name = "risk_class", length = 16)
	@Enumerated(EnumType.STRING)
	private RiskClass riskClass;

	@Column(name = "delta")
	private double delta;

	@Column(name = "vega")
	private double vega;

	@Column(name = "curvature")
	private double curvature;

	@Column(name = "drc_non_sec")
	private double drcNonSec;

	@Column(name = "drc_sec_non_ctp")
	private double drcSecNonCtp;

	@Column(name = "drc_sec_ctp")
	private double drcSecCtp;

	@Column(name = "res_type1")
	private double resType1;

	@Column(name = "res_type2")
	private double resType2;

	public double getTotalRiskCharge() {
		if (RiskClass.RESIDUAL_RISK.equals(getRiskClass())) {
			return resType1 + resType2;
		} else if (RiskClass.DEFAULT_RISK.equals(getRiskClass())) {
			return drcNonSec + drcSecNonCtp + drcSecCtp;
		} else {
			return delta + vega + curvature;
		}
	}

	public void aggregate(RiskClassLevelResults rcl) {
		this.delta += rcl.getDelta();
		this.vega += rcl.getVega();
		this.curvature += rcl.getCurvature();
		this.drcNonSec += rcl.getDrcNonSec();
		this.drcSecCtp += rcl.getDrcSecCtp();
		this.drcSecNonCtp += rcl.getDrcSecNonCtp();
		this.resType1 += rcl.getResType1();
		this.resType2 += rcl.getResType2();
	}
}
