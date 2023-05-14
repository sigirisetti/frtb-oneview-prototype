import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PageUnderConstructionComponent } from './common/page-under-construction/page-under-construction.component';
import { StocksComponent } from './samples/ws/stocks/stocks.component'
import { LineChartComponent } from './samples/charts/line-chart/line-chart.component'
import { ExpandableTableComponent } from './samples/ng-examples/expandable-table/expandable-table.component'
import { LoginComponent } from './core/login/login.component';
import { AuthGuardService } from './core/auth-guard.service';
import { DashboardComponent } from './frtb/dashboard/dashboard.component';
import { SaMrComponent } from './frtb/sa/sa-mr/sa-mr.component';
import { ExecutionListComponent } from './frtb/exec/execution-list/execution-list.component';
import { SaMrSensitivitiesUploadComponent } from './frtb/sa/sa-mr-sensitivities-upload/sa-mr-sensitivities-upload.component';
import { D3BarChartComponent } from './samples/d3/d3-bar-chart/d3-bar-chart.component';
import { D3PieChartComponent } from './samples/d3/d3-pie-chart/d3-pie-chart.component';
import { D3ScatterChartComponent } from './samples/d3/d3-scatter-chart/d3-scatter-chart.component';

const routes: Routes = [
  {path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuardService] },
  {path: 'execDetails', component: ExecutionListComponent, canActivate: [AuthGuardService] },
  {path: 'login', component: LoginComponent },
  {
    path: 'market-data', children: [
      { path: 'forwardPoints', component: PageUnderConstructionComponent, canActivate: [AuthGuardService] 
    },
    ]
  },
  {
    path: 'sa/mr', children: [
      { path: 'sensitivities-upload', component: SaMrSensitivitiesUploadComponent, canActivate: [AuthGuardService] },
      { path: 'all', component: SaMrComponent, canActivate: [AuthGuardService] },
      { path: 'all/:id', component: SaMrComponent, canActivate: [AuthGuardService]},
    ]
  },
  {
    path: 'samples', children: [
      {
        path: 'ws', children: [
          { path: 'stocks', component: StocksComponent },
        ]
      },
      {
        path: 'ng2-charts', children: [
          { path: 'line-chart', component: LineChartComponent },
        ]
      },
      {
        path: 'd3-charts', children: [
          { path: 'bar-chart', component: D3BarChartComponent },
          { path: 'pie-chart', component: D3PieChartComponent },
          { path: 'scatter-chart', component: D3ScatterChartComponent },
        ]
      },
      {
        path: 'ng-examples', children: [
          { path: 'expandable-table-rows', component: ExpandableTableComponent },
        ]
      },
    ]
  },
  {path: '', redirectTo: '/login', pathMatch: 'full'  },
  {path: '**', component: PageUnderConstructionComponent, pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: []
})
export class AppRoutingModule {
}
