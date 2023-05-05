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
        path: 'charts', children: [
          { path: 'line-chart', component: LineChartComponent },
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
