package com.uob.frtb.risk.saccr.results;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SACCRResults implements Serializable {

	private static final long serialVersionUID = -7845079957537472466L;

	private List<String> messages = new ArrayList<>();
	private List<Counterparty> counterparties;

	public void addMessage(String message) {
		messages.add(message);
	}
	
	public boolean hasMessages() {
		return !messages.isEmpty();
	}
}
