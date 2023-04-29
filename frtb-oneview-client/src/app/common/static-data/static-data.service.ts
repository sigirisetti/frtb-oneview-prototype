import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Currency } from './currency';

import * as globals from '../../globals'

@Injectable({
  providedIn: 'root'
})
export class StaticDataService {

  constructor(private http: HttpClient) { 
  }

  getForwardCcyPairs(): Observable<Currency[]> {
    return this.http.get<Currency[]>(globals.forwardCcyPairsUrl);
  }
}
