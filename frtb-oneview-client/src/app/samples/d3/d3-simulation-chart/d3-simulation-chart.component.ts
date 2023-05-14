import { Component } from '@angular/core';
import * as d3 from 'd3';

@Component({
  selector: 'app-d3-simulation-chart',
  templateUrl: './d3-simulation-chart.component.html',
  styleUrls: ['./d3-simulation-chart.component.scss']
})
export class D3SimulationChartComponent {


  private data:any = [];
  private svg: any;
  private margin = 50;
  private width = 750 - (this.margin * 2);
  private height = 400 - (this.margin * 2);

  ngOnInit(): void {
    this.createSvg();
    this.drawPlot();
  }

  private createSvg(): void {
    this.svg = d3.select("figure#scatter")
      .append("svg")
      .attr("width", this.width + (this.margin * 2))
      .attr("height", this.height + (this.margin * 2))
      .append("g")
      .attr("transform", "translate(" + this.margin + "," + this.margin + ")");
  }


  private drawPlot(): void {

  }

}
