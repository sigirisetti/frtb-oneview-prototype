import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef, Component, Input, OnInit, ViewChild } from '@angular/core';
import { MessageDialogService } from 'src/app/common/message-dialog.service';
import { NotificationService } from 'src/app/common/notification.service';
import { WorkflowServiceService } from '../../workflow-service.service';
import { ColDef } from 'ag-grid-community';
import { MatTableDataSource } from '@angular/material/table';
import { WorkflowExecDetail } from 'src/app/model/common/workflow-exec-detail';
import { AgGridBtnCellRendererComponent } from 'src/app/common/ag-grid/ag-grid-btn-cell-renderer/ag-grid-btn-cell-renderer.component';


@Component({
  selector: 'app-execution-list',
  templateUrl: './execution-list.component.html',
  styleUrls: ['./execution-list.component.scss']
})
export class ExecutionListComponent {

  @Input()
  public workflowType = "";
  message: string = 'loading :(';
  domLayout:any = "autoHeight";

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
    private dialogService: MessageDialogService,
    private notificationService: NotificationService,
    private workflowService: WorkflowServiceService
  ) { }

  tableDataSource = new MatTableDataSource<WorkflowExecDetail>();

  columnDefs: ColDef[] = [
    { field: 'date', headerName: 'Date', resizable: true },
    { field: 'workflow.name', headerName: 'Exec Name', resizable: true },
    { field: 'status', headerName: 'Exec Status', resizable: true },
    { field: 'id', headerName: 'Id', cellRenderer: AgGridBtnCellRendererComponent, cellRendererParams: {
      clicked: (field: any) => {
        console.log(field);
      }
    } },
  ];

  ngOnInit() {
    this.workflowService.getAllSAMRWorkflowInstances().subscribe({
      next: (res: WorkflowExecDetail[]) => this.tableDataSource.data = res,
      error: (e) => console.log(e)
    });
  }

  ngAfterViewInit() {
    this.message = 'all done loading :)'
    this.cdr.detectChanges();
  }

}
