import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MessageDialogService } from '../common/message-dialog.service';
import { NotificationService } from '../common/notification.service';
import { Observable } from 'rxjs';
import * as globals from 'src/app/globals';
import { WorkflowExecDetail } from '../model/common/workflow-exec-detail';


@Injectable({
  providedIn: 'root'
})
export class WorkflowServiceService {


  constructor(
    private http: HttpClient,
    private dialogService: MessageDialogService,
    private notificationService: NotificationService,
  ) { }

  
  getAllSAMRWorkflowInstances(): Observable<WorkflowExecDetail[]> {
    return this.http.get<WorkflowExecDetail[]>(globals.getAllSAMRWorkflowInstances);
  }
}
