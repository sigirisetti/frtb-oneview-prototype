import { Component, Input, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { Chart, ChartDataset, ChartConfiguration, ChartEvent, ChartType } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { default as Annotation } from 'chartjs-plugin-annotation';
import { MassQuoteService } from '../../mass-quotes-service'
import { Subscription, Subscriber } from 'rxjs';
import { StaticDataService } from '../../../common/static-data/static-data.service'
import * as globals from '../../../globals'
import { formatDate } from '@angular/common';
import { Inject, LOCALE_ID } from '@angular/core';

@Component({
  selector: 'app-live-series',
  templateUrl: './live-series.component.html',
  styleUrls: ['./live-series.component.css']
})
export class LiveSeriesComponent implements OnInit, OnDestroy {

  @ViewChild(BaseChartDirective, { static: true }) chart: BaseChartDirective;

  @Input()
  public ccyPair = "";

  constructor(
    @Inject(LOCALE_ID) public locale: string,
    private staticDataService: StaticDataService,
    private massQuoteService: MassQuoteService) {
    Chart.register(Annotation)
  }
  private priceTickSub: Subscription;

  public lineChartLegend = true;

  public lineChartLabels: string[] = [];

  public lineChartLabelsInternal: string[] = [];

  public lineChartData: ChartConfiguration['data'] = {
    datasets: [
      {
        data: [],
        label: 'Mkt Bid',
        backgroundColor: 'rgba(0,0,100,0.2)',
        borderColor: 'rgba(148,159,177,1)',
        pointBackgroundColor: 'rgba(148,159,177,1)',
        pointBorderColor: '#fff',
        pointHoverBackgroundColor: '#fff',
        pointHoverBorderColor: 'rgba(148,159,177,0.8)',
        fill: 'origin',
      },
      {
        data: [],
        label: 'Mkt Ask',
        backgroundColor: 'rgba(0,0,100,0.2)',
        borderColor: 'rgba(77,83,96,1)',
        pointBackgroundColor: 'rgba(77,83,96,1)',
        pointBorderColor: '#fff',
        pointHoverBackgroundColor: '#fff',
        pointHoverBorderColor: 'rgba(77,83,96,1)',
        fill: 'origin',
      },
      {
        data: [],
        label: 'Bid',
        backgroundColor: 'rgba(100,0,0,0.3)',
        borderColor: 'red',
        pointBackgroundColor: 'rgba(148,159,177,1)',
        pointBorderColor: '#fff',
        pointHoverBackgroundColor: '#fff',
        pointHoverBorderColor: 'rgba(148,159,177,0.8)',
        fill: 'origin',
      },
      {
        data: [],
        label: 'Ask',
        backgroundColor: 'rgba(100,0,0,0.3)',
        borderColor: 'red',
        pointBackgroundColor: 'rgba(148,159,177,1)',
        pointBorderColor: '#fff',
        pointHoverBackgroundColor: '#fff',
        pointHoverBorderColor: 'rgba(148,159,177,0.8)',
        fill: 'origin',
      }
    ],
    labels: this.lineChartLabels,
  };

  public lineChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      // We use this empty structure as a placeholder for dynamic theming.
    },
    plugins: {
      legend: { display: true },
      annotation: {
        annotations: [],
      },
    },
    elements: {
      line: {
        tension: 0,
        fill: false
      }
    }
  };


  ngOnInit() {
    //console.log("spot price series component initialized : " + this.createTime)
  }

  ngOnDestroy(): void {
    //console.log("spot price series component destroyed!")
  }

  // events
  public chartClicked({ event, active }: { event: MouseEvent, active: {}[] }): void {
    //console.log(event, active);
  }

  public chartHovered({ event, active }: { event: MouseEvent, active: {}[] }): void {
    //console.log(event, active);
  }

  public render() {
    this.getChartData(this.ccyPair);
    this.priceTickSub = this.subscribeToPriceTicks(this.ccyPair);
  }

  public stopRendering() {
    this.priceTickSub.unsubscribe();
  }

  public pushOne(ccyPair: string, tick: any) {
    this.lineChartData.datasets.forEach((x, i) => {
      if (x.data.length == globals.MAX_SERIES_LENGTH) {
        x.data.shift();
      }
      x.data.push(tick[i]);
    })
    if (this.lineChartLabelsInternal.length < globals.MAX_SERIES_LENGTH) {
      this.lineChartLabelsInternal.push(formatDate(new Date(), 'HH:mm:ss.SSS', this.locale));
      //this.lineChartLabelsInternal.push(`T-${this.lineChartLabelsInternal.length}`);
    }else {
      this.lineChartLabelsInternal.shift();
      this.lineChartLabelsInternal.push(formatDate(new Date(), 'HH:mm:ss.SSS', this.locale));  
    }
    this.updateLabelsWithNewLabels();
    console.log("updating chart " + ccyPair + " after : " + tick)
    this.chart.update();
  }

  subscribeToPriceTicks(ccyPair: string): any {
    const subscriber = Subscriber.create(
      (tick) => this.pushOne(ccyPair, tick),
      (error) => { console.log("Failed to subscribe for " + ccyPair + " price series") }
    );
    console.log("Subscribing price ticks for " + ccyPair)
    return this.massQuoteService.subscribeToPriceTicks(ccyPair, subscriber);
  }

  getChartData(ccyPair: string) {
    let chartdataset: ChartDataset[] = this.massQuoteService.getChartData(ccyPair);
    let len = chartdataset[0].data.length;
    if (this.lineChartLabelsInternal.length < globals.MAX_SERIES_LENGTH) {
      this.lineChartLabelsInternal = [];
      for (let i = 0; i < len; i++) {
        this.lineChartLabelsInternal.push(formatDate(new Date(), 'HH:mm:ss.SSS', this.locale));
        //this.lineChartLabelsInternal.push(`T-${i}`);
      }
      this.updateLabelsWithNewLabels();
    }
    chartdataset.forEach((e, i) => {
      this.lineChartData.datasets[i].data.length = 0;
      this.lineChartData.datasets[i].data.push(...e.data);
    })
    this.chart.update();
    console.log("Got chart data for " + ccyPair + " with length " + chartdataset[0].data.length)
  }

  updateLabelsWithNewLabels() {
    this.lineChartLabels.length = 0;
    this.lineChartLabels.push(...this.lineChartLabelsInternal.slice());
  }
}
