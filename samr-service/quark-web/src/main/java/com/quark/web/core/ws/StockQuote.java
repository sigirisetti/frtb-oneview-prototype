package com.quark.web.core.ws;

import lombok.Data;

@Data
public class StockQuote {
    private String symbol;
    private double initialPrice;
    private double price;

    public StockQuote(String symbol, double initialPrice) {
        this.symbol = symbol;
        this.initialPrice = initialPrice;
    }
}
