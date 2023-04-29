import { Injectable } from '@angular/core';
import { Subscription } from 'rxjs';
import { Subject } from "rxjs";
import { StockQuote } from './stock-quote';
import { webSocket } from 'rxjs/webSocket' // for RxJS 6, for v5 use Observable.webSocket
import * as globals from '../../../globals'

@Injectable({
  providedIn: 'root'
})
export class StocksQuoteService {

  sub: Subscription;
  public messages: Subject<StockQuote[]> = new Subject();
  disconnected = false;

  constructor() {
    this.getQuotes();
  }

  getQuotes() {
    let subject = webSocket(globals.samplesUrl);
    subject.subscribe(
        (msg) => this.setData(msg),
        (err) => this.handleError(err),
        () => this.serverDisconnected()
      );
  }

  setData(quotes: any) {
    this.disconnected = false;
    this.messages.next(quotes);
  }

  handleError(err: any) {
    console.log(err)
    this.reconnect()
  }

  serverDisconnected() {
    console.log("Server disconnected")
    this.disconnected = true;
    this.reconnect()
  }

  reconnect() {
    let that = this;
    setTimeout(function() {
      console.log("Trying to reconnect..")
      that.getQuotes();
    }, 2000);
  }

}
