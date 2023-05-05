import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MessageDialogComponent } from './message-dialog/message-dialog.component';
import { map, take } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class MessageDialogService {

  constructor(private dialog: MatDialog) { }
  dialogRef: MatDialogRef<MessageDialogComponent>;
  public open(options: any) {

    this.dialogRef = this.dialog.open(MessageDialogComponent, {
      data: {
        title: options.title,
        message: options.message,
        confirmText: options.confirmText,
      }
    });
  }

  
  public confirmed(): Observable<any> {
    return this.dialogRef.afterClosed().pipe(take(1), map(res => {
      return res;
    }
    ));
  }

  showErrorMessage(title: string, msg: string) {
    const options = {
      title: title == undefined ? 'Error' : title,
      message: msg,
      confirmText: 'Ok'
    }

    this.open(options);

    this.confirmed().subscribe((confirmed: any) => {
      if (confirmed) {
        //do nothing
      }
    });
  }
}
