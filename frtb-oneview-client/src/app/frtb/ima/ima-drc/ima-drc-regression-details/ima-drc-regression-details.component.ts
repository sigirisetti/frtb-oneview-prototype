import { HttpClient } from '@angular/common/http';
import { Component, ViewChild } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MatCalendarCellClassFunction } from '@angular/material/datepicker';
import { MessageDialogService } from 'src/app/common/message-dialog.service';
import { NotificationService } from 'src/app/common/notification.service';

@Component({
  selector: 'app-ima-drc-regression-details',
  templateUrl: './ima-drc-regression-details.component.html',
  styleUrls: ['./ima-drc-regression-details.component.scss']
})
export class ImaDrcRegressionDetailsComponent {

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
  
  constructor(
    private http: HttpClient,
    private dialogService: MessageDialogService,
    private notificationService: NotificationService,
  ) { }

  ngOnInit(): void {
    
  }

  reset() {
    this.formGroup.patchValue({ valueDate: new Date() });
  }

  onSubmit() {
    this.dialogService.showErrorMessage("TODO", "Not yet implemented")
  }
}
