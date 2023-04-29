import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import { PageUnderConstructionComponent } from './common/page-under-construction/page-under-construction.component';
import {StocksComponent} from './samples/ws/stocks/stocks.component'
import { LineChartComponent } from './samples/charts/line-chart/line-chart.component'
import { ExpandableTableComponent } from './samples/ng-examples/expandable-table/expandable-table.component'


const routes: Routes = [
  {path: '', component: PageUnderConstructionComponent, pathMatch: 'full'},
  {path: 'market-data', children: [
    {path: 'forwardPoints', component: PageUnderConstructionComponent},
  ]},
  {path: 'samples', children: [
    {path: 'ws', children: [
      {path: 'stocks', component: StocksComponent},
    ]},
    {path: 'charts', children: [
      {path: 'line-chart', component: LineChartComponent},
    ]},
    {path: 'ng-examples', children: [
      {path: 'expandable-table-rows', component: ExpandableTableComponent},
    ]},
  ]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: []
})
export class AppRoutingModule {
}
