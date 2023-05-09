package com.quark.core.scheduling;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "q_scheduled_task_properties", uniqueConstraints = @UniqueConstraint(columnNames = { "scheduled_task_id",
		"name" }))
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "q_sch_task_prop_id_seq")
public class ScheduledTaskProperties implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
	private Long id;

	@Column(length = 32, nullable = false)
	private String name;

	@Column(length = 32, nullable = false)
	private String value;

	@ManyToOne
	@JoinColumn(name = "scheduled_task_id")
	private ScheduleTaskConfig scheduledTaskConfig;
}
