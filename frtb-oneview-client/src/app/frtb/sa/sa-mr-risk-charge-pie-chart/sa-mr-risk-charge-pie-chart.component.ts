import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, ContentChildren, ViewChild } from '@angular/core';
import { MessageDialogService } from 'src/app/common/message-dialog.service';
import { WorkflowServiceService } from '../../workflow-service.service';
import { NotificationService } from 'src/app/common/notification.service';
import { WorkflowExecDetail } from 'src/app/model/common/workflow-exec-detail';
import { SaMrService } from '../sa-mr.service';
import { SamrDashboardData } from 'src/app/model/common/samr/samr-dashboard-data';
import { PieChartComponent } from 'src/app/common/ng2-charts/pie-chart/pie-chart.component';

@Component({
  selector: 'app-sa-mr-risk-charge-pie-chart',
  templateUrl: './sa-mr-risk-charge-pie-chart.component.html',
  styleUrls: ['./sa-mr-risk-charge-pie-chart.component.scss']
})
export class SaMrRiskChargePieChartComponent {

  @ViewChild(PieChartComponent) chart: PieChartComponent | undefined;

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
              let totalRiskCharge = [];

              for (let rt of res.riskClassLevelResults) {
                this.labels.push(rt.riskClass);
                totalRiskCharge.push(Math.round(rt.totalRiskCharge))
              }
            
              this.dataSets.push({ data: totalRiskCharge, label: 'Risk Charge' });
              console.log(this.chart)
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
