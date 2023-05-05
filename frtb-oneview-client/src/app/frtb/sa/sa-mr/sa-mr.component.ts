import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MessageDialogService } from 'src/app/common/message-dialog.service';
import { AuthService } from 'src/app/core/auth.service';
import * as globals from 'src/app/globals';
import { WorkflowServiceService } from '../../workflow-service.service';
import { NotificationService } from 'src/app/common/notification.service';

@Component({
  selector: 'app-sa-mr',
  templateUrl: './sa-mr.component.html',
  styleUrls: ['./sa-mr.component.scss']
})
export class SaMrComponent {

  constructor(
    private http: HttpClient,
    private dialogService: MessageDialogService,
    private notificationService: NotificationService,
    private workflowServiceService: WorkflowServiceService
  ) { }


  ngOnInit() {
    this.workflowServiceService.getAllSAMRWorkflowInstances().subscribe({
      next: (res: any) => console.log(res),
      error: (e:any) => this.notificationService.error$.next('Error in getting execution')
    });
  }

  formGroup = new FormGroup({
    valueDate: new FormControl(new Date()),
    workflowType: new FormControl('')
  });


  onSubmit() {
    let formVal = this.formGroup.value;
    if (formVal.valueDate === null || formVal.valueDate === null || formVal.workflowType === '' || formVal.workflowType === '') {
      this.dialogService.showErrorMessage('No Execution Details', 'Please enter execution details');
      return;
    }
    this.http.get<any>(globals.loginUrl).subscribe({
      next: (res: any) => console.log(res),
      error: (e) => console.log(e)
    });
  }


}
