import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-snack-bar',
  templateUrl: './snack-bar.component.html',
  styleUrls: ['./snack-bar.component.css']
})
export class SnackBarComponent implements OnInit {

  ngOnInit() {
  }

  durationInSeconds = 5;

  constructor(private _snackBar: MatSnackBar) { }

  openSnackBar(c: any) {
    this._snackBar.openFromComponent(c, {
      duration: this.durationInSeconds * 1000,
    });
  }
}
