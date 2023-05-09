package com.quark.trade;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SwapLeg implements Serializable {

	private static final long serialVersionUID = -5999655560699193165L;

    private enum FixedOrFloat {
		FIXED, FLOAT
    }

    private enum PayOrRecieve {
		PAYER, RECEIVER
    }

    private FixedOrFloat fixedOrFloat;
	private PayOrRecieve payOrRecieve;
	private Double fixedRate;
	private String iborIndex;
	private String dayCounter;
	private String paymentBDC;
	private String paymentFrequency;
	private String effectiveDateConvention;
	private String terminationDateConvention;
	private String generationRule;
	private Boolean endOfMonth;
	private Date firstDate;
	private Date nextToLastDate;
}
