package com.quark.risk.frtb.samr.csv.model;

import com.quark.risk.frtb.samr.model.Hierarchy;
import com.quark.risk.frtb.samr.model.RiskClassAndSensitivity;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class HierarchyData extends SAMRData<Hierarchy> {

	private static final long serialVersionUID = 1L;

	private String po;
	private String product;
	private String desk;
	private String book;
	private String location;

	@Override
	protected RiskClassAndSensitivity getRiskClassAndSensitivity() {
		return null;
	}

	@Override
	public List<Hierarchy> buildTradeData() {

		if (StringUtils.isBlank(getTradeIdentifier())) {
			addMessage("Trade identifier is empty");
		}
		if (StringUtils.isBlank(getPo())) {
			addMessage("PO is not specified");
		}
		if (StringUtils.isBlank(getProduct())) {
			addMessage("Product is not specified");
		}
		if (StringUtils.isBlank(getDesk())) {
			addMessage("Desk is not specified");
		}
		if (StringUtils.isBlank(getBook())) {
			addMessage("Book is not specified");
		}
		if (StringUtils.isBlank(getLocation())) {
			addMessage("Location is not specified");
		}
		if (getMessages() != null) {
			return Collections.emptyList();
		}
		Hierarchy h = new Hierarchy();
		h.setTradeIdentifier(getTradeIdentifier());
		h.setPo(getPo());
		h.setProduct(getProduct());
		h.setDesk(getDesk());
		h.setBook(getBook());
		h.setLocation(getLocation());
		return Arrays.asList(h);
	}
}
