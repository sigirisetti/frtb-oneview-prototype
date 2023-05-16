package com.uob.frtb.risk.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uob.frtb.core.security.Organization;
import com.uob.frtb.core.workflow.WorkflowConfig;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "q_workflow_instance")
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowInstance implements Serializable {

	private static final long serialVersionUID = 721562210460474921L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", length = 36, unique = true)
	private String id;

	@Column(name = "excel_date")
	private int excelDate;

	@Transient
	private Date date;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "workflow_config_id")
	private WorkflowConfig workflow;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "organization_id")
	private Organization organization;

	@Column(name = "status", length = 10)
	private WorkflowInstExecStatus status;

	@JsonIgnore
	@OneToMany(mappedBy = "workflowInstance", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	private Set<WorkflowInstanceProperties> properties;
	
	@Column
	@Type(type = "yes_no")
	private Boolean deleted;
}
