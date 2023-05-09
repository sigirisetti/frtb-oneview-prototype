import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MessageDialogService } from 'src/app/common/message-dialog.service';
import { AuthService } from 'src/app/core/auth.service';
import * as globals from 'src/app/globals';
import { WorkflowServiceService } from '../../workflow-service.service';
import { NotificationService } from 'src/app/common/notification.service';
import { MatTableDataSource } from '@angular/material/table';
import { WorkflowExecDetail } from 'src/app/model/common/workflow-exec-detail';
import { ColDef } from 'ag-grid-community';
import { AgGridBtnCellRendererComponent } from 'src/app/common/ag-grid/ag-grid-btn-cell-renderer/ag-grid-btn-cell-renderer.component';
import { formatDate, formatNumber } from '@angular/common';
import { SaMrService } from '../sa-mr.service';
import { FlatTreeControl } from '@angular/cdk/tree';
import { MatTreeFlatDataSource, MatTreeFlattener } from '@angular/material/tree';
import { SamrResultLineItem } from 'src/app/model/common/samr/samr-result-line-item';


interface SamrTreeFlatNode {
  expandable: boolean;
  level: number;

  identifier: string;
  nodeName: string;
  amountLowCorr: number;
  amountBaseLowCorr: number;
  amount: number;
  amountBase: number;
  amountHighCorr: number;
  amountBaseHighCorr: number;
};

@Component({
  selector: 'app-sa-mr',
  templateUrl: './sa-mr.component.html',
  styleUrls: ['./sa-mr.component.scss']
})
export class SaMrComponent {

  domLayout: any = "autoHeight";
  totalRiskChargeBaseLowCorr:number = 0;
  totalRiskChargeBase:number = 0;
  totalRiskChargeBaseHighCorr:number = 0;

  constructor(
    private http: HttpClient,
    private dialogService: MessageDialogService,
    private notificationService: NotificationService,
    private workflowServiceService: WorkflowServiceService,
    private saMrService: SaMrService,
  ) { }

  tableDataSource = new MatTableDataSource<WorkflowExecDetail>();

  columnDefs: ColDef[] = [
    {
      field: 'date', headerName: 'Date', resizable: true, valueFormatter: function (params) {
        return formatDate(params.value, 'yyyy-MM-dd', 'en-US');
        //return params.value;
      }
    },
    { field: 'workflow.name', headerName: 'Exec Name', resizable: true },
    { field: 'workflow.organization.name', headerName: 'Entity', resizable: true },
    { field: 'status', headerName: 'Exec Status', resizable: true },
    {
      field: 'id', headerName: 'Id', cellRenderer: AgGridBtnCellRendererComponent, cellRendererParams: {
        clicked: (field: any) => {
          this.displayResults(field);
        }
      }
    },
  ];


  hasChild = (_: number, node: SamrTreeFlatNode) => node.expandable;

  displayedColumns: string[] = ['nodeName', 'amountLowCorr', 'amountLowCorr', 
  'amount', 'amountBase',  'amountHighCorr', 'amountBaseHighCorr',];

  private transformer = (node: SamrResultLineItem, level: number) => {
    console.log(node);
    return {
      expandable: !!node.children && node.children.length > 0,
      level: level,

      identifier: node.identifier,
      nodeName: node.nodeName,
      amountLowCorr: node.amountLowCorr,
      amountBaseLowCorr: node.amountBaseLowCorr,
      amount: node.amount,
      amountBase: node.amountBase,
      amountHighCorr: node.amountHighCorr,
      amountBaseHighCorr: node.amountBaseHighCorr,
    };
  };

  treeControl = new FlatTreeControl<SamrTreeFlatNode>(node => node.level, node => node.expandable);

  treeFlattener = new MatTreeFlattener(this.transformer, node => node.level,
    node => node.expandable, node => node.children);

  samrDataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);
  ngOnInit() {
    this.workflowServiceService.getAllSAMRWorkflowInstances().subscribe({
      next: (res: any) => this.tableDataSource.data = res,
      error: (e: any) => this.notificationService.error$.next('Error in getting execution')
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

  displayResults(id: string) {
    for (let r of this.tableDataSource.data) {
      if (r.id === id) {
        console.log(r)
        this.saMrService.getSAMRExecResults(r.date, r.workflow.id).subscribe({
          next: (res: any) => {
            this.totalRiskChargeBase = res.totalRiskCharge;
            this.samrDataSource.data = res.results;
          },
          error: (e) => console.log(e)
        });
      }
    }
  }

}
