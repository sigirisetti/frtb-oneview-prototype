import { TestBed } from '@angular/core/testing';

import { StocksQuoteService } from './stocks-quote.service';

describe('StocksQuoteService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: StocksQuoteService = TestBed.get(StocksQuoteService);
    expect(service).toBeTruthy();
  });
});
