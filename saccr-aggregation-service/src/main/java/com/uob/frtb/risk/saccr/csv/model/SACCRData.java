package com.uob.frtb.risk.saccr.csv.model;

import com.uob.frtb.risk.common.model.BaseRiskEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public abstract class SACCRData<T extends BaseRiskEntity> implements Serializable {

	private static final long serialVersionUID = -9189401649091809036L;

	protected List<String> messages;

	public void addMessage(String msg) {
		if (messages == null) {
			messages = new ArrayList<>();
		}
		messages.add(msg);
	}

	public abstract T build();
}
