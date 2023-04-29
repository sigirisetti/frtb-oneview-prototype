import { Component, OnInit } from '@angular/core';
import { StocksQuoteService } from './stocks-quote.service'
import { StockQuote } from './stock-quote'

@Component({
  selector: 'app-stocks',
  templateUrl: './stocks.component.html',
  styleUrls: ['./stocks.component.css']
})
export class StocksComponent implements OnInit {

  stockQuotes: StockQuote[];
  displayedColumns = ["symbol", "price"];

  constructor(private stocksQuoteService: StocksQuoteService) {
  }

  ngOnInit() {
    this.stocksQuoteService.messages.subscribe(msg => {
      this.stockQuotes = msg;
    });
  }

  onRowClicked(row: any) {
  }

}
