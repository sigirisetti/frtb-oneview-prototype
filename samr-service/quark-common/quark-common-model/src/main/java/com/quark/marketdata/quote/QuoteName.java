package com.quark.marketdata.quote;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "quote_name")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "quote")
public class QuoteName implements Serializable {

	private static final long serialVersionUID = -5646192450987892652L;

	@Id
	@Column(name = "quote_name", length = 128)
	private String quoteName;

	@Column(length = 16)
	private String type;
}
