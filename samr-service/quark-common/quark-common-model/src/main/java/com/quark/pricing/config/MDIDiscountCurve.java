package com.quark.pricing.config;

import com.quark.marketdata.curve.Curve;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "mdi_discount_curve")
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "mdi_id_seq")
public class MDIDiscountCurve extends MarketDataItem implements Serializable {

	private static final long serialVersionUID = 8404033528524270117L;

	@ManyToOne
	@JoinColumn(name = "discount_curve_id", referencedColumnName = "id", nullable = false)
	private Curve discountCurve;

	public MDIDiscountCurve() {
		setType(Type.D.toString());
	}

	public MDIDiscountCurve(Curve discountCurve) {
		setType(Type.D.toString());
		this.discountCurve = discountCurve;
	}

	@Override
	public MDIIdentifier getIdentifier() {
		return super.getIdentifier();
	}

}
