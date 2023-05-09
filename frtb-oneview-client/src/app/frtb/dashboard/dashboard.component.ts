import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { MessageDialogService } from 'src/app/common/message-dialog.service';
import { NotificationService } from 'src/app/common/notification.service';
import { WorkflowServiceService } from '../workflow-service.service';
import { SaMrService } from '../sa/sa-mr.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {

  constructor(
    private http: HttpClient,
    private dialogService: MessageDialogService,
    private notificationService: NotificationService,
    private workflowServiceService: WorkflowServiceService,
    private saMrService: SaMrService,
  ) { }

}
