package com.quark.pricing.config;

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
@Table(name = "pricing_engine_config", uniqueConstraints = @UniqueConstraint(columnNames = {
		"pricing_engine_config_name" }))
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "pe_config_id_seq")
public class PricingEngineConfig implements Serializable {

	private static final long serialVersionUID = -4915580527568969242L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
	private Long id;

	@Column(name = "pricing_engine_config_name", length = 32, nullable = false)
	private String pricingEngineConfigName;

	@OneToMany(mappedBy = "pricingEngineConfig", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private Set<PricingEngineConfigItem> configItems;

	public PricingEngineConfig(String name) {
		this.pricingEngineConfigName = name;
	}

	public void addPricingEngineConfigItem(PricingEngineConfigItem pricingEngineConfigItem) {
		if (configItems == null) {
			configItems = new HashSet<>();
		}
		configItems.add(pricingEngineConfigItem);
		pricingEngineConfigItem.setPricingEngineConfig(this);
	}
}
