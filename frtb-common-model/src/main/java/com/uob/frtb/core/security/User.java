package com.uob.frtb.core.security;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "q_user", uniqueConstraints = @UniqueConstraint(columnNames = { "email" }))
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "q_user_id_seq")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "security")
public class User implements Serializable {

	private static final long serialVersionUID = 1110037685880265171L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
	private Long id;

	@Column(length = 32, nullable = false)
	private String name;

	@Column(length = 32, nullable = false)
	private String email;

	@Column(length = 256, nullable = false)
	private String password;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name="organization_id")
	private Organization organization;

	@Type(type = "yes_no")
	@Column
	private boolean active;

	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@JoinTable(name = "q_user_group_map", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "group_id") })
	private Set<Group> groups;

	public void addGroup(Group g1) {
		if (groups == null) {
			groups = new HashSet<>();
		}
		groups.add(g1);
	}

	public User(String name, String email, String password, Organization organization, boolean active,
			Set<Group> groups) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.organization = organization;
		this.active = active;
		this.groups = groups;
	}
}
