package com.uob.frtb.risk.samr.model;

import com.uob.frtb.core.exception.ApplicationException;
import com.uob.frtb.risk.common.model.WorkflowInstance;

import java.sql.Date;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CalcRequest {

	private Date valueDate;
	private String baseCurrency;
	private WorkflowInstance workflowInstance;

	private List<TradeSensitivity> tradeSensitivities = Collections.emptyList();
	private List<DRCNonSec> drcNonSecData = Collections.emptyList();
	private List<DRCSecNonCTP> drcSecNonCTPData = Collections.emptyList();
	private List<DRCSecCTP> drcSecCTPData = Collections.emptyList();
	private List<Residuals> residuals = Collections.emptyList();

	private List<SamrMarketQuote> quotes = Collections.emptyList();
	private List<EquityInfo> equityInfo = Collections.emptyList();
	private List<CreditIssuerInfo> creditIssuerInfo = Collections.emptyList();
	private List<CommodityInfo> commodityInfo = Collections.emptyList();

	private boolean persistResults;

	public void validate() throws ApplicationException {
		if (tradeSensitivities.isEmpty() && drcNonSecData.isEmpty() && drcSecNonCTPData.isEmpty()
				&& drcSecCTPData.isEmpty() && residuals.isEmpty()) {
			throw new ApplicationException(-1,
					String.format("No data to process for workflow Id: %s", workflowInstance.getId()));
		}
	}

	public CalcRequest filterDataByTradeIds(List<String> tradeIds, CalcRequest request) {
		CalcRequest req = new CalcRequest();
		req.setWorkflowInstance(request.getWorkflowInstance());
		req.setTradeSensitivities(request.getTradeSensitivities().stream()
				.filter(t -> tradeIds.contains(t.getTradeIdentifier())).collect(Collectors.toList()));
		req.setDrcNonSecData(request.getDrcNonSecData().stream().filter(t -> tradeIds.contains(t.getTradeIdentifier()))
				.collect(Collectors.toList()));
		req.setDrcSecNonCTPData(request.getDrcSecNonCTPData().stream()
				.filter(t -> tradeIds.contains(t.getTradeIdentifier())).collect(Collectors.toList()));
		req.setDrcSecCTPData(request.getDrcSecCTPData().stream().filter(t -> tradeIds.contains(t.getTradeIdentifier()))
				.collect(Collectors.toList()));
		req.setResiduals(request.getResiduals().stream().filter(t -> tradeIds.contains(t.getTradeIdentifier()))
				.collect(Collectors.toList()));
		req.setBaseCurrency(request.getBaseCurrency());
		req.setQuotes(request.getQuotes());
		req.setEquityInfo(request.getEquityInfo());
		req.setCreditIssuerInfo(request.getCreditIssuerInfo());
		req.setCommodityInfo(request.getCommodityInfo());
		req.setPersistResults(request.isPersistResults());
		return req;
	}

	public CalcRequest filterOrphenedTrades(Map<String, List<String>> poMap, CalcRequest fullReq) {
		CalcRequest req = new CalcRequest();
		Set<String> tagged = new HashSet<>();
		for (List<String> ids : poMap.values()) {
			tagged.addAll(ids);
		}
		req.setWorkflowInstance(fullReq.getWorkflowInstance());
		req.setTradeSensitivities(fullReq.getTradeSensitivities().stream()
				.filter(t -> !tagged.contains(t.getTradeIdentifier())).collect(Collectors.toList()));
		req.setDrcNonSecData(fullReq.getDrcNonSecData().stream().filter(t -> !tagged.contains(t.getTradeIdentifier()))
				.collect(Collectors.toList()));
		req.setDrcSecNonCTPData(fullReq.getDrcSecNonCTPData().stream()
				.filter(t -> !tagged.contains(t.getTradeIdentifier())).collect(Collectors.toList()));
		req.setDrcSecCTPData(fullReq.getDrcSecCTPData().stream().filter(t -> !tagged.contains(t.getTradeIdentifier()))
				.collect(Collectors.toList()));
		req.setResiduals(fullReq.getResiduals().stream().filter(t -> !tagged.contains(t.getTradeIdentifier()))
				.collect(Collectors.toList()));
		req.setBaseCurrency(fullReq.getBaseCurrency());
		req.setQuotes(fullReq.getQuotes());
		req.setEquityInfo(fullReq.getEquityInfo());
		req.setCreditIssuerInfo(fullReq.getCreditIssuerInfo());
		req.setCommodityInfo(fullReq.getCommodityInfo());
		req.setPersistResults(fullReq.isPersistResults());
		return req;
	}
}
