package com.uob.frtb.risk.samr.results;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uob.frtb.risk.samr.model.RiskClass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.SerializationUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "frtb_samr_inter_res")
public class IntermediateResultEntity implements Serializable {

	private static final long serialVersionUID = 3969011942299350376L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", length = 36, unique = true)
	private String id;

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "po_results_id")
	private PoResults poResults;

	@Enumerated(EnumType.STRING)
	@Column(name = "risk_class", length = 16, nullable = false)
	private RiskClass riskClass;

	@Column(name = "sensitivity_type", length = 16, nullable = false)
	private String sensitivityType;

	@Column(name = "currency", length = 16, nullable = false)
	private String currency;

	@Column(name = "rate_index", length = 16, nullable = false)
	private String rateIndex;

	@JsonIgnore
	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "result_data")
	private byte[] resultData;

	@Transient
	private IntermediateResult results;

	public void setResults(IntermediateResult results) {
		this.results = results;
		this.resultData = SerializationUtils.serialize(results);
	}

	public IntermediateResult getResults() {
		if (this.results == null) {
			this.results = SerializationUtils.deserialize(resultData);
		}
		return results;
	}
}
