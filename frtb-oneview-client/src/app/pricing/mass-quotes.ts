export class MassQuote {
    symbol: string;

    tradeDate: string;
    spotDate: String;
    settleDate: Date;

    marketBid: number;
    marketAsk: number;
    bid: number;
    ask: number;
    
    exchangeId: number;
    customerId: string;
    sessionId: string;
    spread: number;
}