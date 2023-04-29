package com.ssk.ng.xva.websocket;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MarketData {

    private String symbol;
    private Date settleDate;
    private double marketBid;
    private double marketAsk;

}
