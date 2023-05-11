import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, ContentChildren, ViewChild } from '@angular/core';
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
import { SamrDashboardData } from 'src/app/model/common/samr/samr-dashboard-data';
import { BaseChartDirective } from 'ng2-charts';
import { BarChartComponent } from 'src/app/common/ng2-charts/bar-chart/bar-chart.component';

@Component({
  selector: 'app-sa-mr-risk-charge-bar-chart',
  templateUrl: './sa-mr-risk-charge-bar-chart.component.html',
  styleUrls: ['./sa-mr-risk-charge-bar-chart.component.scss']
})
export class SaMrRiskChargeBarChartComponent {

  @ViewChild(BarChartComponent) chart: BarChartComponent | undefined;

  samrExecutions: WorkflowExecDetail[] = [];

  labels: any = [];

  dataSets: any = [];

  constructor(
    private http: HttpClient,
    private dialogService: MessageDialogService,
    private notificationService: NotificationService,
    private workflowServiceService: WorkflowServiceService,
    private saMrService: SaMrService,
  ) { }


  ngOnInit() {
    this.workflowServiceService.getAllSAMRWorkflowInstances().subscribe({
      next: (res: any) => {
        this.samrExecutions = res;
        if (res.length > 0) {
          let r = res[res.length - 1];
          this.saMrService.getSamrDashboardData(r.date, r.workflow.id).subscribe({
            next: (res: SamrDashboardData) => {
              let delta = [];
              let vega = [];
              let curvature = [];
              let drcNonSec = [];
              let drcSecNonCtp = [];
              let drcSecCtp = [];
              let resType1 = [];
              let resType2 = [];

              for (let rt of res.riskClassLevelResults) {
                this.labels.push(rt.riskClass);
                delta.push(Math.round(rt.delta))
                vega.push(Math.round(rt.vega))
                curvature.push(Math.round(rt.curvature))
                drcNonSec.push(Math.round(rt.drcNonSec))
                drcSecNonCtp.push(Math.round(rt.drcSecNonCtp))
                drcSecCtp.push(Math.round(rt.drcSecCtp))
                resType1.push(Math.round(rt.resType1))
                resType2.push(Math.round(rt.resType2))

              }
              console.log(delta)
              console.log(vega)
              console.log(curvature)
              console.log(drcNonSec)
              console.log(drcSecNonCtp)
              console.log(drcSecCtp)
              console.log(resType1)
              console.log(resType2)
              
              this.dataSets.push({ data: delta, label: 'delta' });
              this.dataSets.push({ data: vega, label: 'vega' });
              this.dataSets.push({ data: curvature, label: 'curvature' });
              this.dataSets.push({ data: drcNonSec, label: 'drcNonSec' });
              this.dataSets.push({ data: drcSecNonCtp, label: 'drcSecNonCtp' });
              this.dataSets.push({ data: drcSecCtp, label: 'drcSecCtp' });
              this.dataSets.push({ data: resType1, label: 'resType1' });
              this.dataSets.push({ data: resType2, label: 'resType2' });

              this.chart?.update()
            },
            error: (e) => console.log(e)
          });
        }
      },
      error: (e: any) => this.notificationService.error$.next('Error in getting execution')
    });
  }
}
