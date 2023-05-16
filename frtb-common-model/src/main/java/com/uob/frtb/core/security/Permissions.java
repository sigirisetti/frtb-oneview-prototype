package com.uob.frtb.core.security;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
@Table(name = "q_permissions", uniqueConstraints = @UniqueConstraint(columnNames = { "type", "name" }))
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "q_permissions_id_seq")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "security")
public class Permissions implements Serializable {

	private static final long serialVersionUID = 6043533290140295178L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
	private Long id;

	@Column(length = 16, nullable = false)
	private String type;

	@Column(length = 32, nullable = false)
	private String name;

	public Permissions(String type, String name) {
		this.type = type;
		this.name = name;
	}
}
