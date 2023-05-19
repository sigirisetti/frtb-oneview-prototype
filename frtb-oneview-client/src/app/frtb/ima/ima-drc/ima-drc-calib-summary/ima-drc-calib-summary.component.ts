import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MatCalendarCellClassFunction } from '@angular/material/datepicker';
import { MatTableDataSource } from '@angular/material/table';
import { ColDef } from 'ag-grid-community';
import { MessageDialogService } from 'src/app/common/message-dialog.service';
import { NotificationService } from 'src/app/common/notification.service';
import { DrcCalibSummary } from 'src/app/model/ima/drc-calib-summary';

@Component({
  selector: 'app-ima-drc-calib-summary',
  templateUrl: './ima-drc-calib-summary.component.html',
  styleUrls: ['./ima-drc-calib-summary.component.scss']
})
export class ImaDrcCalibSummaryComponent {

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

  tableDataSource = new MatTableDataSource<DrcCalibSummary>();

  columnDefs: ColDef[] = [
    { field: 'issuer', headerName: 'Issuer', resizable: true, filter: true },
    { field: 'beta1', headerName: 'Beta1 (Index)', resizable: true, sortable: true },
    { field: 'beta2', headerName: 'Beta2 (Sector)', resizable: true, sortable: true },
    { field: 'residualVol', headerName: 'Residual Vol', resizable: true, sortable: true },
    { field: 'factorCorr', headerName: 'Factor Correlation', resizable: true, sortable: true },
    { field: 'rSquare', headerName: 'R Square', resizable: true, sortable: true },
    { field: 'normalizationFactor', headerName: 'Normalization Factor', resizable: true, sortable: true },
  ];

  constructor(
    private http: HttpClient,
    private dialogService: MessageDialogService,
    private notificationService: NotificationService,
  ) { }

  ngOnInit() {
    this.tableDataSource.data = [
      { issuer: 'XOM', beta1: -0.411, beta2: 0.982, residualVol: 0.074, factorCorr: 0.501, rSquare: 0.923, normalizationFactor: 0.856, },
      { issuer: 'CVX', beta1: -0.295, beta2: 0.790, residualVol: 0.0796, factorCorr: 0.501, rSquare: 0.871, normalizationFactor: 0.695, },
      { issuer: 'BAC', beta1: -0.605, beta2: 1.822, residualVol: 0.008, factorCorr: 0.866, rSquare: 0.863, normalizationFactor: 1.332, },
      { issuer: 'JPM', beta1: -0.238, beta2: 1.194, residualVol: 0.07, factorCorr: 0.866, rSquare: 0.865, normalizationFactor: 0.997, },
      { issuer: 'AAPL', beta1: -0.396, beta2: 2.539, residualVol: 0.129, factorCorr: 0.842, rSquare: 0.737, normalizationFactor: 1.562, },
      { issuer: 'MSFT', beta1: -0.358, beta2: 1.022, residualVol: 0.074, factorCorr: 0.842, rSquare: 0.675, normalizationFactor: 0.749, },
    ];
  }

  reset() {
    this.formGroup.patchValue({valueDate: new Date()});
  }

  onSubmit() {
    this.dialogService.showErrorMessage("TODO", "Not yet implemented")
  }
}
