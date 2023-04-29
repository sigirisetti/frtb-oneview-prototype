import { Injectable } from '@angular/core';
import { ChConnConfig } from 'src/app/common/model/ch-conn-config';

@Injectable({
  providedIn: 'root'
})
export class GlobalStateService {

  selectedEnv: ChConnConfig;

  constructor() { }

  setSelectedEnv(selectedEnv : ChConnConfig) {
    this.selectedEnv = selectedEnv;
  }

  getSelectedEnv() : ChConnConfig {
    return this.selectedEnv;
  }

}
