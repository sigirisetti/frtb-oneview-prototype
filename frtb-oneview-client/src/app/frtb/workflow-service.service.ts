import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import * as globals from 'src/app/globals';
import { MessageDialogService } from 'src/app/common/message-dialog.service';
import { NotificationService } from 'src/app/common/notification.service';
import { WorkflowExecDetail } from 'src/app/model/common/workflow-exec-detail';
import { WorkflowConfig } from 'src/app/model/common/workflow-config';


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

  getSAMRWorkflows(): Observable<WorkflowConfig[]> {
    return this.http.get<WorkflowConfig[]>(globals.getSAMRWorkflows);
  }
}
