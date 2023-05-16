package com.uob.frtb.common;

import com.uob.frtb.core.security.Organization;
import com.uob.frtb.core.security.User;
import com.uob.frtb.risk.common.model.WorkflowInstance;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "q_exec_info")
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "exec_info_id_seq")
public class ExecInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
	private Long id;

	@Column(name = "exec_type_id")
	private Integer execTypeId;

	@Column(name = "exec_timestamp", updatable = false)
	private Timestamp executionTime;

	@Column(name = "valuation_timestamp", updatable = false)
	private Timestamp valuationDatetime;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "organization_id")
	private Organization organization;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "workflow_id")
	private WorkflowInstance workflowInstance;

	@Column(name = "sch_task_id", nullable = true)
	private Long schTaskId;
}
