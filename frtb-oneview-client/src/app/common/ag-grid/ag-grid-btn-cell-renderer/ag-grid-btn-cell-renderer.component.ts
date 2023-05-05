import { Component, Input } from '@angular/core';
import { ICellRendererAngularComp } from 'ag-grid-angular';
import { ICellRendererParams } from 'ag-grid-community';

@Component({
  selector: 'app-ag-grid-btn-cell-renderer',
  templateUrl: './ag-grid-btn-cell-renderer.component.html',
  styleUrls: ['./ag-grid-btn-cell-renderer.component.scss']
})
export class AgGridBtnCellRendererComponent implements ICellRendererAngularComp {

  @Input()
  public label = "";

  private params: any;

  agInit(params: any): void {
    this.params = params;
  }

  btnClickHandler(event: any) {
    this.params.clicked(this.params.value);
  }

  refresh(params: ICellRendererParams<any>) {
    return true;
  }
}
