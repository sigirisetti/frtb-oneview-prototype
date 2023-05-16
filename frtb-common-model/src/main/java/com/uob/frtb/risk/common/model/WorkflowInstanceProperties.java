package com.uob.frtb.risk.common.model;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@NoArgsConstructor
@Entity
@Table(name = "q_workflow_instance_properties")
public class WorkflowInstanceProperties implements Serializable {

	private static final long serialVersionUID = -1042145763155942958L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", length = 36, unique = true)
	private String id;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "workflow_instance_id")
	private WorkflowInstance workflowInstance;

	@Column(name = "file_or_dir_name", length = 64, nullable = false)
	private String name;

	@Column(name = "path", length = 512, nullable = false)
	private String value;

	public WorkflowInstanceProperties(WorkflowInstance workflowInstance, String name, String value) {
		this.workflowInstance = workflowInstance;
		this.name = name;
		this.value = value;
	}
}
