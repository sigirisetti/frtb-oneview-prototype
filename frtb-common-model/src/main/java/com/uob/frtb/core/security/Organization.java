package com.uob.frtb.core.security;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "q_organization", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "q_group_id_seq")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "security")
public class Organization implements Serializable {

	private static final long serialVersionUID = -4776235910723720627L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
	private Long id;

	@Column(length = 32)
	private String name;

	@Column
	@Type(type = "yes_no")
	private boolean active;

	public Organization(String name, boolean active) {
		this.name = name;
		this.active = active;
	}
}
