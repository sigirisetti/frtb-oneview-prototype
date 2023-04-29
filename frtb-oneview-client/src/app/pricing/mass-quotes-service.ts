import { Injectable, OnInit } from '@angular/core';

import { Subject, Observable, Subscription, combineLatest, zip, Subscriber } from 'rxjs';
import { map, concat } from 'rxjs/operators';
import {  } from "rxjs";
import { webSocket } from 'rxjs/webSocket' // for RxJS 6, for v5 use Observable.webSocket
import * as globals from '../globals'
import { ChartDataset, ChartOptions } from 'chart.js';
import CircularBuffer from 'circularbuffer'

import { MassQuote } from './mass-quotes';
import { Currency } from '../common/static-data/currency';

import { StaticDataService } from '../common/static-data/static-data.service'
import { NotificationService } from '../common/notification.service'

@Injectable({
    providedIn: 'root'
})
export class MassQuoteService implements OnInit {

    disconnected = false;

    public messages: Subject<MassQuote> = new Subject();
    private massQuotes: Map<string, Array<MassQuote>> = new Map();
    private currencies: Currency[];

    private mktBidSubjectMap: Map<String, Subject<number>> = new Map();
    private mktAskSubjectMap: Map<String, Subject<number>> = new Map();
    private bidSubjectMap: Map<String, Subject<number>> = new Map();
    private askSubjectMap: Map<String, Subject<number>> = new Map();
    private spreadSubjectMap: Map<String, Subject<number>> = new Map();

    private ccyPairBufferMap: Map<String, CircularBuffer<number>[]> = new Map();
    private initialchartingDataSetsMap: Map<String, ChartDataset[]> = new Map();

    private tickSubjectMap: Map<string, Subject<number[]>> = new Map();
    private tickObsMap: Map<string, Observable<number[]>> = new Map();

    constructor(private staticDataService: StaticDataService, private notificationService: NotificationService) {
        this.getMassQuotes();
    }

    ngOnInit() {
    }

    getMassQuotes() {

        this.staticDataService.getForwardCcyPairs().subscribe(
            (res: Currency[]) => {
                this.currencies = res;
                for (let currency of res) {
                    let cp = currency.symbol;
                    let mktBidSubject: Subject<number> = new Subject();
                    this.mktBidSubjectMap.set(cp, mktBidSubject);

                    let mktAskSubject: Subject<number> = new Subject();
                    this.mktAskSubjectMap.set(cp, mktAskSubject);

                    let bidSubject: Subject<number> = new Subject();
                    this.bidSubjectMap.set(cp, bidSubject);

                    let askSubject: Subject<number> = new Subject();
                    this.askSubjectMap.set(cp, askSubject);

                    let spreadSubject: Subject<number> = new Subject();
                    this.spreadSubjectMap.set(cp, spreadSubject);

                    //let pricesSeries: Map<string, Observable<number[]>> = new Map();

                    let tickSubject: Subject<number[]> = new Subject();

                    let chartDataSets: CircularBuffer<number>[] = [
                        new CircularBuffer(globals.MAX_SERIES_LENGTH),
                        new CircularBuffer(globals.MAX_SERIES_LENGTH),
                        new CircularBuffer(globals.MAX_SERIES_LENGTH),
                        new CircularBuffer(globals.MAX_SERIES_LENGTH),
                        //new CircularBuffer(globals.MAX_SERIES_LENGTH)
                    ];

                    let initialchartDataSets: ChartDataset[] = [
                        { data: [], label: 'Mkt Bid' },
                        { data: [], label: 'Mkt Ask' },
                        { data: [], label: 'Bid' },
                        { data: [], label: 'Ask' },
                        //{ data: [], label: 'Spread' }
                    ];
                    this.initialchartingDataSetsMap.set(cp, initialchartDataSets);
                    this.ccyPairBufferMap.set(cp, chartDataSets);

                    let obs = combineLatest(zip(mktBidSubject.asObservable(), mktAskSubject.asObservable()),
                        zip(bidSubject.asObservable(), askSubject.asObservable(), spreadSubject.asObservable())).pipe(map(a => a[0].concat(a[1])));

                    this.tickSubjectMap.set(cp, tickSubject);
                    console.log("Added tick obs map entry for " + cp)
                    this.tickObsMap.set(cp, tickSubject.asObservable());

                    obs.subscribe((t) => {
                        tickSubject.next(t);
                        let buffers = this.ccyPairBufferMap.get(cp);
                        let ds = this.initialchartingDataSetsMap.get(cp);
                        buffers?.forEach((e, i) => {
                            e.enq(t[i]);
                            if(ds != undefined) {
                                ds[i].data  = e.toArray();
                            }
                        });
                        //console.log("Got tick for " + cp + ". Current series length = " + ds[0].data.length)
                    });
                }
            },
            error => {
                this.notificationService.error$.next("Currencies rest api error : " + error.message);
                console.log("Currencies rest api error : " + error)
            }
        );


        let subject = webSocket(globals.massQuotesUrl);
        subject.subscribe(
            (msg) => this.setData(msg),
            (err) => this.handleError(err),
            () => this.serverDisconnected()
        );
    }

    setData(q: any) {
        this.disconnected = false;
        let payload = q.payload;
        let key = this.getKey(payload);
        let arr = this.massQuotes.get(key);
        if (arr != null) {
            for (let entry of arr) {
                this.updateData(q.dataType, entry, payload)
            }
        } else {
            arr = new Array();
            let massquote = new MassQuote();
            this.updateData(q.dataType, massquote, payload)
            arr.push(massquote);
            this.massQuotes.set(key, arr);
        }
    }

    updateData(dataType : string, massquote: MassQuote, payload: MassQuote) {
        massquote.symbol = payload.symbol;
        massquote.settleDate = payload.settleDate;

        if (dataType === "com.ssk.ng.frtb-oneview.websocket.MarketData") {
            //console.log("Market data")
            massquote.marketBid = payload.marketBid;
            this.mktBidSubjectMap.get(massquote.symbol)?.next(massquote.marketBid);
            massquote.marketAsk = payload.marketAsk;
            this.mktAskSubjectMap.get(massquote.symbol)?.next(massquote.marketAsk);
        } else if (dataType === "com.ssk.ng.frtb-oneview.websocket.MassQuotes") {
            //console.log("Mass Quote")
            massquote.exchangeId = payload.exchangeId;
            massquote.customerId = payload.customerId;
            massquote.sessionId = payload.sessionId;
            //massquote.pricingProfileId = payload.pricingProfileId;
            //massquote.instrumentType = payload.instrumentType;
            //massquote.deliverable = payload.deliverable;
            massquote.spotDate = payload.spotDate;

            massquote.bid = payload.bid;
            this.bidSubjectMap.get(massquote.symbol)?.next(massquote.bid);
            massquote.ask = payload.ask;
            this.askSubjectMap.get(massquote.symbol)?.next(massquote.ask);
            massquote.spread = payload.spread;
            this.spreadSubjectMap.get(massquote.symbol)?.next(massquote.spread);
        } else {
            this.notificationService.error$.next("Unknown datatype : " + dataType);
            console.log("Unknown datatype : " + dataType)
        }
        this.messages.next(massquote);
    }

    getKey<MassQuote>(massquote: any) {
        return massquote.symbol;
    }

    handleError(err: any) {
        console.log(err)
        this.reconnect()
    }

    serverDisconnected() {
        console.log("Server disconnected")
        this.notificationService.error$.next("Server disconnected");
        this.disconnected = true;
        this.reconnect()
    }

    reconnect() {
        let that = this;
        setTimeout(function () {
            that.notificationService.error$.next("Trying to reconnect to mass quotes web socket ..");
            console.log("Trying to reconnect to mass quotes web socket ..")
            that.getMassQuotes();
        }, 2000);
    }

    public subscribeToPriceTicks(ccyPair : string, subscriber: any) {
        return this.tickObsMap.get(ccyPair)?.subscribe(subscriber);
    }

    public getChartData(ccyPair: string) : any {
        return this.initialchartingDataSetsMap.get(ccyPair);
    }

    public getCurrencies() {
        return this.currencies;
    }
}
