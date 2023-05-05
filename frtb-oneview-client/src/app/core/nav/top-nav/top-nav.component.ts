import { Component, OnInit } from '@angular/core';
import {NavService} from '../nav.service';
import {FormControl} from '@angular/forms';
import { EnvService } from '../env.service';
import { ChConnConfig } from 'src/app/common/model/ch-conn-config';
import { GlobalStateService } from 'src/app/common/global-state.service';
import { AuthService } from '../../auth.service';
import { Entity } from 'src/app/model/common/entity';


@Component({
  selector: 'app-top-nav',
  templateUrl: './top-nav.component.html',
  styleUrls: ['./top-nav.component.scss']
})
export class TopNavComponent implements OnInit {

  instance = new FormControl('');
  connConfigs: ChConnConfig[] = [];


  constructor(
    public navService: NavService, 
    public clickchouseEnvService: EnvService,
    public globalStateService: GlobalStateService,
    public authService: AuthService 
    ) { 
  }

  ngOnInit() {
    /*
    this.clickchouseEnvService.chConnConfig().subscribe((connConfigs: ChConnConfig[]) => {
      this.connConfigs = connConfigs;
    });
    */
  }

  selectInstance(entity: Entity) {
    this.globalStateService.setSelectedEntity(entity);
  }
}