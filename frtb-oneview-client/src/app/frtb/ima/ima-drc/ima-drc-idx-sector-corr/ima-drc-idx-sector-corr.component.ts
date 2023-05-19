import { HttpClient } from '@angular/common/http';
import { Component, ViewChild } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MatCalendarCellClassFunction } from '@angular/material/datepicker';
import { D3CorrelogramComponent } from 'src/app/common/d3/d3-correlogram/d3-correlogram.component';
import { MessageDialogService } from 'src/app/common/message-dialog.service';
import { NotificationService } from 'src/app/common/notification.service';

@Component({
  selector: 'app-ima-drc-idx-sector-corr',
  templateUrl: './ima-drc-idx-sector-corr.component.html',
  styleUrls: ['./ima-drc-idx-sector-corr.component.scss']
})
export class ImaDrcIdxSectorCorrComponent {

  @ViewChild(D3CorrelogramComponent) corrComp: D3CorrelogramComponent;

  valueDate: Date = new Date();
  today = new Date();

  holidayDateFilter = (d: Date | null): boolean => {
    if (d?.getDay() === 0 || d?.getDay() === 6) {
      return false;
    }
    return true;
  };

  dateClass: MatCalendarCellClassFunction<Date> = (cellDate, view) => {
    if (view === 'month') {
      const date = cellDate.getDate();
      return date === 1 || date === 20 ? 'example-custom-date-class' : '';
    }
    return '';
  };


  formGroup = new FormGroup({
    valueDate: new FormControl(new Date()),
  });

  columnsToDisplay: string[] = [];
  columnNamesToDisplay: any = [];
  labels: string[] = ['S&P 500', 'S&P 500 Energy', 'S&P 500 Financials', 'S&P 500 Information Technology'];

  dataMatrix: number[][] = [
    [1, 0, 0, 0],
    [0.5, 1, 0, 0],
    [0.87, 0.68, 1, 0],
    [0.84, 0.13, 0.57, 1]
  ];

  data: any[] = [
    { x: 'S&P 500', y: 'S&P 500', value: 1 },
    { x: 'S&P 500', y: 'S&P 500 Energy', value: 0.5 },
    { x: 'S&P 500', y: 'S&P 500 Financials', value: 0.87 },
    { x: 'S&P 500', y: 'S&P 500 Information Technology', value: 0.84 },
    { x: 'S&P 500 Energy', y: 'S&P 500 Energy', value: 1 },
    { x: 'S&P 500 Energy', y: 'S&P 500 Financials', value: 0.68 },
    { x: 'S&P 500 Energy', y: 'S&P 500 Information Technology', value: 0.13 },
    { x: 'S&P 500 Financials', y: 'S&P 500 Financials', value: 1 },
    { x: 'S&P 500 Financials', y: 'S&P 500 Information Technology', value: 0.57 },
    { x: 'S&P 500 Information Technology', y: 'S&P 500 Information Technology', value: 1 },
  ];

  constructor(
    private http: HttpClient,
    private dialogService: MessageDialogService,
    private notificationService: NotificationService,
  ) { }

  ngOnInit(): void {
    for (let i = 0; i <= this.dataMatrix.length; i++) {
      this.columnsToDisplay.push('c' + i);
      if (i === 0) {
        this.columnNamesToDisplay['c' + i] = '';
      } else {
        this.columnNamesToDisplay['c' + i] = this.labels[i - 1];
      }
    }
    let data: any[] = [];
    for (let i = 0; i < this.dataMatrix.length; i++) {
      data[i] = {};
      data[i]['c' + 0] = this.labels[i];
      for (let j = 1; j < this.columnsToDisplay.length; j++) {
        data[i]['c' + j] = this.dataMatrix[i][j - 1];
      }
    }
    this.data = data;
    console.log(this.data)
  }

  showCorr() {
    this.corrComp.drawCorr(this.data);
  }


  reset() {
    this.formGroup.patchValue({valueDate: new Date()});
  }
  
  onSubmit() {
    this.dialogService.showErrorMessage("TODO", "Not yet implemented")
  }
}
