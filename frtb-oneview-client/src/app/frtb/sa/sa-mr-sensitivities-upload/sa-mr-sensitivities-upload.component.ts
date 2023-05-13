import { HttpClient, HttpErrorResponse, HttpEvent, HttpEventType, HttpHeaders, HttpParams, HttpRequest } from '@angular/common/http';
import { Component } from '@angular/core';
import { Observable, catchError, last, map, tap, throwError } from 'rxjs';
import * as globals from 'src/app/globals';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatCalendarCellClassFunction } from '@angular/material/datepicker';
import { WorkflowServiceService } from '../../workflow-service.service';
import { MessageDialogService } from 'src/app/common/message-dialog.service';
import { NotificationService } from 'src/app/common/notification.service';
import { WorkflowConfig } from 'src/app/model/common/workflow-config';

@Component({
  selector: 'app-sa-mr-sensitivities-upload',
  templateUrl: './sa-mr-sensitivities-upload.component.html',
  styleUrls: ['./sa-mr-sensitivities-upload.component.scss']
})
export class SaMrSensitivitiesUploadComponent {

  myFiles: string[] = [];

  selectedFiles: FileList;
  currentFile: File;
  progress = 0;
  message = '';

  fileInfos: Observable<any>;
  workflowConfig: WorkflowConfig;

  today = new Date();

  holidayDateFilter = (d: Date | null): boolean => {
    if (d?.getDay() === 0 || d?.getDay() === 6) {
      return false;
    }
    return true;
  };

  dateClass: MatCalendarCellClassFunction<Date> = (cellDate, view) => {
    if (view === 'month') {
      const date = cellDate.getDate();
      return date === 1 || date === 20 ? 'example-custom-date-class' : '';
    }
    return '';
  };

  uploadForm = new FormGroup({
    valueDate: new FormControl(new Date()),
    display: new FormControl("", Validators.required),
    workflowId: new FormControl(0),
  });

  constructor(
    private http: HttpClient,
    private formBuilder: FormBuilder,
    private dialogService: MessageDialogService,
    private notificationService: NotificationService,
    private workflowService: WorkflowServiceService
  ) { }

  ngOnInit() {
    this.workflowService.getSAMRWorkflows().subscribe({
      next: (res: WorkflowConfig[]) => {
        console.log(res[0])
        this.workflowConfig = res[0]
        this.uploadForm.patchValue({
          workflowId: res[0].id
        });
      },
      error: (e: any) => this.notificationService.error$.next('Error in getting workflow config')
    });


  }

  /*
  selectFile(event: any) {
    this.selectedFiles = event.target.files;
  }
  */

  handleFileInputChange(l: FileList): void {
    console.log(l)
    this.selectedFiles = l;
    for (let i = 0; i < l.length; i++) {
      //console.log(l[i]);
    }
    if (l.length) {
      const f = l[0];
      const count = l.length > 1 ? `(+${l.length - 1} files)` : "";
      this.uploadForm.patchValue({ display: `${f.name}${count}` });
    } else {
      this.uploadForm.patchValue({ display: "" });
    }
  }

  onSubmit() {
    this.progress = 0;

    console.log("Upload file length : " + this.selectedFiles.length)
    if (this.selectedFiles && this.selectedFiles.length === 0) {
      return;
    }

    this.currentFile = this.selectedFiles.item(0)!;

    const formData: any = new FormData();
    formData.append("valueDate", "2023-05-12");
    formData.append("workflowId", "1");
    formData.append("files", this.selectedFiles);

    /*
    const newrequest = new HttpRequest('POST', globals.samrFileUpload, formData, {
      reportProgress: true,
      responseType: 'json'
    });
    */

    //console.log("posting files")
    /*
    this.http.request(newrequest).pipe(
      map(event => this.getEventMessage(event)),
      tap(message => this.showProgress(message)),
      last(), // return last (completed) message to caller
      catchError(e => this.handleError(e))
    );
    */
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'multipart/form-data'
      }),
    };

    this.http.post<any>(globals.samrFileUpload, formData).subscribe({
      next: (res: any) => {
        console.log(res)
      },
      error: (e) => console.log(e)
    });
    return true;
  }

  /** Return distinct message for sent, upload progress, & response events */
  private getEventMessage(event: HttpEvent<any>) {
    switch (event.type) {
      case HttpEventType.Sent:
        return `Uploading to backend`;

      case HttpEventType.UploadProgress:
        // Compute and show the % done:
        const percentDone = event.total ? Math.round(100 * event.loaded / event.total) : 0;
        return `Files upload is ${percentDone}% uploaded.`;

      case HttpEventType.Response:
        return `Files uploaded successfully!`;

      default:
        return `Un-expected upload event: ${event.type}.`;
    }
  }

  showProgress(msg: any) {
    console.log(msg);
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(`Backend returned code ${error.status}, body was: `, error.error);
    }
    // Return an observable with a user-facing error message.
    return throwError(() => new Error('Something bad happened; please try again later.'));
  }
}
