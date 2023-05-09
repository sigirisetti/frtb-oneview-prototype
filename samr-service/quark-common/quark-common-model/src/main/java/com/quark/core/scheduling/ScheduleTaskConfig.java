package com.quark.core.scheduling;

import com.quark.core.security.Organization;

import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "q_scheduled_task_config", uniqueConstraints = @UniqueConstraint(columnNames = { "name",
		"organization_id" }))
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "q_scheduled_task_config_id_seq")
public class ScheduleTaskConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
	private Long id;

	@Column(length = 32, nullable = false)
	private String name;

	@Column(length = 32, nullable = false)
	private String type;

	@Column(length = 64, nullable = false)
	private String cronExpression;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "organization_id")
	private Organization organization;

	@Type(type = "yes_no")
	@Column
	private boolean enabled;

	@OneToMany(mappedBy = "scheduledTaskConfig", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private Set<ScheduledTaskProperties> properties;
}
