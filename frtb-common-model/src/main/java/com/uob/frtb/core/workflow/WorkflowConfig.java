package com.uob.frtb.core.workflow;

import com.uob.frtb.core.security.Organization;

import org.hibernate.annotations.Type;

import java.io.Serializable;

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
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "q_workflow_config", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "organization_id", "process" }))
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "q_workflow_config_id_seq")
public class WorkflowConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
	private Long id;

	@Column(length = 32, nullable = false)
	private String name;

	@Column(length = 32, nullable = false)
	private String process;

	@Column(length = 64, nullable = false)
	private String cronExpression;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "organization_id")
	private Organization organization;

	@Type(type = "yes_no")
	@Column
	private boolean enabled;
}
