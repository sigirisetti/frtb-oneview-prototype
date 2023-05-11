import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MessageDialogService } from 'src/app/common/message-dialog.service';
import { NotificationService } from 'src/app/common/notification.service';
import * as globals from 'src/app/globals';
import { SamrResult } from 'src/app/model/common/samr/samr-result';
import { formatDate, formatNumber } from '@angular/common';
import { SamrDashboardData } from 'src/app/model/common/samr/samr-dashboard-data';

@Injectable({
  providedIn: 'root'
})
export class SaMrService {

  constructor(
    private http: HttpClient,
    private dialogService: MessageDialogService,
    private notificationService: NotificationService,
  ) { }


  getSAMRExecResults(valueDate: Date, workflowId: number): Observable<SamrResult> {
    const options = {
      params: new HttpParams()
        .set('valueDate', formatDate(valueDate, 'yyyy-MM-dd', 'en-US'))
        .set('workflowId', workflowId)
    };
    return this.http.get<SamrResult>(globals.getSAMRExecResults, options);
  }

  getSamrDashboardData(valueDate: Date, workflowId: number): Observable<SamrDashboardData> {
    const options = {
      params: new HttpParams()
        .set('valueDate', formatDate(valueDate, 'yyyy-MM-dd', 'en-US'))
        .set('workflowId', workflowId)
    };
    return this.http.get<SamrDashboardData>(globals.getSamrDashboardData, options);
  }

}
