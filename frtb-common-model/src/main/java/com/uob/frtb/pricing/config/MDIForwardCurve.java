package com.uob.frtb.pricing.config;

import com.uob.frtb.marketdata.curve.Curve;

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
@Table(name = "mdi_forward_curve")
@SequenceGenerator(initialValue = 1, name = "idgen", sequenceName = "mdi_id_seq")
public class MDIForwardCurve extends MarketDataItem {

	private static final long serialVersionUID = 3294045929198751901L;

	@ManyToOne
	@JoinColumn(name = "forward_curve_id", referencedColumnName = "id", nullable = false)
	private Curve forwardCurve;

	@Override
	public MDIIdentifier getIdentifier() {
		return super.getIdentifier();
	}
}
