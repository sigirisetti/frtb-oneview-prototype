import { Component } from '@angular/core';
import * as d3 from 'd3';

@Component({
  selector: 'app-d3-correlogram',
  templateUrl: './d3-correlogram.component.html',
  styleUrls: ['./d3-correlogram.component.scss']
})
export class D3CorrelogramComponent {


  private svg: any;
  private margin = 20;
  private width = 150 - (this.margin * 2);
  private height = 150 - (this.margin * 2);

  ngOnInit(): void {
    this.createSvg();
    //this.drawCorr();
  }

  private createSvg(): void {

    // Create the svg area
    this.svg = d3.select("#corr")
      .append("svg")
      .attr("width", this.width + this.margin + this.margin)
      .attr("height", this.height + this.margin + this.margin)
      .append("g")
      .attr("transform", "translate(" + this.margin + "," + this.margin + ")");
  }

  drawCorr(data: any) {
    //this.svg.selectAll("*").remove();

    // List of all variables and number of them
    var domain: string[] = Array.from(data.map(function (d: any) { return d.x }))
    var num = Math.sqrt(data.length)

    // Create a color scale
    var color = d3.scaleLinear<string, number>()
      .domain([-1, 0, 1])
      .range(["#B22222", "#fff", "#000080"]);

    // Create a size scale for bubbles on top right. Watch out: must be a rootscale!
    var size = d3.scaleSqrt()
      .domain([0, 1])
      .range([0, 9]);

    // X scale
    var x = d3.scalePoint()
      .range([0, this.width])
      .domain(domain)

    // Y scale
    var y = d3.scalePoint()
      .range([0, this.height])
      .domain(domain)

    // Create one 'g' element for each cell of the correlogram
    var cor = this.svg.selectAll(".cor")
      .data(data)
      .enter()
      .append("g")
      .attr("class", "cor")
      .attr("transform", function (d: any) {
        return "translate(" + x(d.x) + "," + y(d.y) + ")";
      });

    // Low left part + Diagonal: Add the text with specific color
    cor
      .filter(function (d: any) {
        var ypos = domain.indexOf(d.y);
        var xpos = domain.indexOf(d.x);
        return xpos <= ypos;
      })
      .append("text")
      .attr("y", 5)
      .text(function (d: any) {
        if (d.x === d.y) {
          return d.x;
        } else {
          return d.value.toFixed(2);
        }
      })
      .style("font-size", 11)
      .style("text-align", "center")
      .style("fill", function (d: any) {
        if (d.x === d.y) {
          return "#000";
        } else {
          return color(d.value);
        }
      });


    // Up right part: add circles
    cor
      .filter(function (d: any) {
        var ypos = domain.indexOf(d.y);
        var xpos = domain.indexOf(d.x);
        return xpos > ypos;
      })
      .append("circle")
      .attr("r", function (d: any) { return size(Math.abs(d.value)) })
      .style("fill", function (d: any) {
        if (d.x === d.y) {
          return "#000";
        } else {
          return color(d.value);
        }
      })
      .style("opacity", 0.8)
  }
}
