package com.quark.pricing.config;

import java.io.Serializable;
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
@Table(name = "market_data_set", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "market_data_set_id_seq")
public class MarketDataSet implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgen")
	private Long id;

	@Column(length = 32)
	private String name;

	@OneToMany(mappedBy = "marketDataSet", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private Set<MDIDiscountCurve> discountCurves;

	@OneToMany(mappedBy = "marketDataSet", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	private Set<MDIForwardCurve> forwardCurves;

	public MarketDataSet(String name) {
		this.name = name;
	}

	public MDIDiscountCurve getDiscountCurve(String currency, String productType) {
		MDIIdentifier id = new MDIIdentifier(currency, productType);
		for (MDIDiscountCurve mdi : discountCurves) {
			if (mdi.getIdentifier().equals(id)) {
				return mdi;
			}
		}
		return null;
	}

	public MDIForwardCurve getForwardCurve(String currency, String productType) {
		MDIIdentifier id = new MDIIdentifier(currency, productType);
		for (MDIForwardCurve mdi : forwardCurves) {
			if (mdi.getIdentifier().equals(id)) {
				return mdi;
			}
		}
		return null;
	}

	public void addDiscountCurve(MDIDiscountCurve curve) {
		discountCurves.add(curve);
	}

	public void addForwardCurve(MDIForwardCurve curve) {
		forwardCurves.add(curve);
	}
}
