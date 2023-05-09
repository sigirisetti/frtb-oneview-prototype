package com.quark.trade;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trade implements Serializable {

	private static final long serialVersionUID = -1943955537905421239L;

	private String holidayCalendar;
	private String createdBy;
	private Date createdDate;
	private String updatedBy;
	private Date lastUpdated;
	private int version;
}
