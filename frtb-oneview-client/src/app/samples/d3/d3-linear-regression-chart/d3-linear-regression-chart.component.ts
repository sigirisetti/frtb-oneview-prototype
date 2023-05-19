import { Component } from '@angular/core';
import { map, zip } from 'rxjs';
import * as d3 from 'd3';
import * as ss from 'simple-statistics';

@Component({
  selector: 'app-d3-linear-regression-chart',
  templateUrl: './d3-linear-regression-chart.component.html',
  styleUrls: ['./d3-linear-regression-chart.component.scss']
})
export class D3LinearRegressionChartComponent {

  yearsExperience: number[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11];
  salaries: number[] = [39343, 46205, 37731, 43525, 39891, 56642, 60150, 54445, 64445, 57189, 63218]
  data: any = zip(this.yearsExperience, this.salaries).pipe(map(([x, y]) => ({ x, y })));
  margin: any = ({ top: 20, right: 20, bottom: 20, left: 50 });

  private svg: any;
  private width = 750 - (this.margin * 2);
  private height = 400 - (this.margin * 2);

  ngOnInit(): void {
    this.createSvg(this.data);
  }

  private createSvg(data: any[]): void {
    // Create the X-axis band scale
    const x = d3.scaleBand()
      .range([0, this.width])
      .domain(data.map((d: any) => d.Framework))
      .padding(0.2);

    // Draw the X-axis on the DOM
    this.svg.append("g")
      .attr("transform", "translate(0," + this.height + ")")
      .call(d3.axisBottom(x))
      .selectAll("text")
      .attr("transform", "translate(-10,0)rotate(-45)")
      .style("text-anchor", "end");



    const xScale = d3.scaleLinear()
      .domain([0, d3.max(data, d => d.x)])
      .range([this.margin.left, this.width - this.margin.right])

    const yScale = d3.scaleLinear()
      .domain([0, 20000 + d3.max(data, d => d.y)]) // added a bit of breathing room (20,000)
      .range([this.height - this.margin.bottom, this.margin.top])

    const yAxis = (g: any) => g.attr('transform', `translate(${this.margin.left}, 0)`)
      .attr("class", "yAxis")
      .call(d3.axisLeft(yScale))

    // We get back an object with m (slope) and b (y intercept). Inspect the object above if you're not sure.
    const linearRegression = ss.linearRegression(data.map(d => [d.x, d.y]))

    // We can pass that object into a helper function to get a function that given x, returns y!
    // It's just using the formula from our high school algebra class, y = mx + b
    const linearRegressionLine = ss.linearRegressionLine(linearRegression)

    // We need to define the 2 points of the regression line to be able to have D3 make a line.
    // This just makes 2 points, 1 for the start and 1 for the end of our line.

    const firstX = data[0].x;
    const lastX = data.slice(-1)[0].x;
    const xCoordinates = [firstX, lastX];
    const points = xCoordinates.map(d => ({
      x: d,                         // We pick x and y arbitrarily, just make sure they match d3.line accessors
      y: linearRegressionLine(d)
    }));

    const regressionPoints = {
      //return points;
    };
  }
}
