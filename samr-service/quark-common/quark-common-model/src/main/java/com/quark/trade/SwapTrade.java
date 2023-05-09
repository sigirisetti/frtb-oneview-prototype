package com.quark.trade;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SwapTrade extends Trade {

	private static final long serialVersionUID = 5481530815857088397L;

	private Double nominal;
	private Date effectiveDate;
	private Date terminationDate;
	private Boolean payer;
	private Double npv;

	// Fixed Leg
	private SwapLeg fixedLeg;
	private SwapLeg floatLeg;
}
