package com.quark.core.security;

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
@Table(name = "q_user_group", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "q_user_group_id_seq")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "security")
public class Group implements Serializable {

	private static final long serialVersionUID = -3325529272065704780L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
	private Long id;

	@Column(length = 32, nullable = false)
	private String name;

	@Column
	@Type(type = "yes_no")
	private boolean active;

	@ManyToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id")
	private Group parent;

	@OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
	private Set<Group> children = new HashSet<>();

	public Group(String name, boolean active, Group parent, Set<Group> children) {
		this.name = name;
		this.active = active;
		this.parent = parent;
		this.children = children;
	}
}
