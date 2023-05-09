package com.quark.marketdata.curve;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "curve_underlying_map", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "curve_id", "underlying_id" }) })
public class CurveUnderlyingMap implements Serializable {

	private static final long serialVersionUID = -3746756661695976171L;

	@Id
	@Column(name = "curve_id")
	private Long curveId;

	@Id
	@Column(name = "underlying_id")
	private Long underlyingId;
}
