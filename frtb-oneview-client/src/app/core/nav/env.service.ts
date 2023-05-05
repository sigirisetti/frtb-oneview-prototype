import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as globals from '../../globals'
import { GlobalStateService } from 'src/app/common/global-state.service';
import { Entity } from 'src/app/model/common/entity';

@Injectable({
  providedIn: 'root'
})
export class EnvService {

  constructor(private http: HttpClient, private globalStateService: GlobalStateService) {
  }

  getAllEntities(): Observable<Entity[]> {
    return this.http.get<Entity[]>(globals.entityUrl);
  }

  setSelectedEntity(selectedEnv: Entity) {
    this.http.put<Entity>(globals.setSelectedEntityUrl, selectedEnv)
      .subscribe(data => console.log(data));
    this.globalStateService.setSelectedEntity(selectedEnv);
  }
}
