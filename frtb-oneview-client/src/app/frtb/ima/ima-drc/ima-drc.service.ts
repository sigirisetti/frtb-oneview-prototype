import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MessageDialogService } from 'src/app/common/message-dialog.service';
import { NotificationService } from 'src/app/common/notification.service';
import * as globals from 'src/app/globals';
import { DrcCalibSummary } from 'src/app/model/ima/drc-calib-summary';

@Injectable({
  providedIn: 'root'
})
export class ImaDrcService {


  constructor(
    private http: HttpClient,
    private dialogService: MessageDialogService,
    private notificationService: NotificationService,
  ) { }


  getDrcCalibSummary(): Observable<DrcCalibSummary[]> {
    return this.http.get<DrcCalibSummary[]>(globals.getDrcCalibSummary);
  }  

}
