import { Injectable } from '@angular/core';
import { ChConnConfig } from 'src/app/common/model/ch-conn-config';
import { Entity } from '../model/common/entity';

@Injectable({
  providedIn: 'root'
})
export class GlobalStateService {

  selectedEntity: Entity;

  constructor() { }

  setSelectedEntity(selectedEntity : Entity) {
    this.selectedEntity = selectedEntity;
  }

  getSelectedEntity() : Entity {
    return this.selectedEntity;
  }
}
