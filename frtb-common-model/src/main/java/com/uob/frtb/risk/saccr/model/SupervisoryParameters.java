package com.uob.frtb.risk.saccr.model;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "saccr_supervisory_parameters")
public class SupervisoryParameters implements Serializable {

	private static final long serialVersionUID = 5102896033399399544L;

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", length = 36, unique = true)
	protected String id;

	@Enumerated(EnumType.STRING)
	@Column(name = "asset_class", length = 8, nullable = false)
	private AssetClass assetClass;

	@Enumerated(EnumType.STRING)
	@Column(name = "sn_or_index", length = 16, nullable = false)
	private SingleNameOrIndex singleNameOrIndex;

	@Column(name = "sub_class", length = 64)
	private String subClass;

	@Column(name = "supervisory_factor")
	private double supervisoryFactor;

	@Column(name = "correlation")
	private double correlation;

	@Column(name = "supervisory_option_vol")
	private double supervisoryOptionVol;
}
