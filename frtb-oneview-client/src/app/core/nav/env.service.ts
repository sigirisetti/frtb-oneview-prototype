import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ChConnConfig } from 'src/app/common/model/ch-conn-config';
import * as globals from '../../globals'
import { GlobalStateService } from 'src/app/common/global-state.service';

@Injectable({
  providedIn: 'root'
})
export class EnvService {

  constructor(private http: HttpClient, private globalStateService: GlobalStateService) {
  }

  chConnConfig(): Observable<ChConnConfig[]> {
    return this.http.get<ChConnConfig[]>(globals.chConnConfig);
  }

  setSelectedEnv(selectedEnv: ChConnConfig) {
    this.http.put<ChConnConfig>(globals.setSelectedEnv, selectedEnv)
      .subscribe(data => console.log(data));
    this.globalStateService.setSelectedEnv(selectedEnv);
  }
}
