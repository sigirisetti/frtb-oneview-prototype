package com.quark.risk.frtb.samr.results;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class IntermediateResult implements Serializable {
	private static final long serialVersionUID = 1L;

	private double kb;
	private double kbBase;
	private double sb;
	private double sbBase;

	public abstract List<String> headers();

	public abstract List<Object> data();
}
