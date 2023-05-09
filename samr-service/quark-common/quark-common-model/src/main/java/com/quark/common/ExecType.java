package com.quark.common;

import lombok.Getter;

@Getter
public enum ExecType {

	RISK_FRTB_SAMR_FILE_UPLOAD(400, "SAMR File Upload"), RISK_FRTB_SAMR_CALCULATION(400, "SAMR Calculation");

	private Integer typeId;
	private String name;

    ExecType(Integer typeId, String name) {
		this.typeId = typeId;
		this.name = name;
	}
}
