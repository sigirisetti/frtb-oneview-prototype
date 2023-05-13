import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { MatToolbarModule } from '@angular/material/toolbar';

import { A11yModule } from '@angular/cdk/a11y';
import { ClipboardModule } from '@angular/cdk/clipboard';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { PortalModule } from '@angular/cdk/portal';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { CdkStepperModule } from '@angular/cdk/stepper';
import { CdkTableModule } from '@angular/cdk/table';
import { CdkTreeModule } from '@angular/cdk/tree';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatBadgeModule } from '@angular/material/badge';
import { MatBottomSheetModule } from '@angular/material/bottom-sheet';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatChipsModule } from '@angular/material/chips';
import { MatStepperModule } from '@angular/material/stepper';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatNativeDateModule, MatRippleModule } from '@angular/material/core';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatSliderModule } from '@angular/material/slider';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatTreeModule } from '@angular/material/tree';
import { OverlayModule } from '@angular/cdk/overlay';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';


import { NgChartsModule } from 'ng2-charts';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { PageUnderConstructionComponent } from './common/page-under-construction/page-under-construction.component';
import { SnackBarComponent } from './common/snack-bar/snack-bar.component';
import { MenuListItemComponent } from './core/nav/menu-list-item/menu-list-item.component';
import { TopNavComponent } from './core/nav/top-nav/top-nav.component';
import { LineChartComponent } from './samples/charts/line-chart/line-chart.component';
import { ExpandableTableComponent } from './samples/ng-examples/expandable-table/expandable-table.component';
import { StocksComponent } from './samples/ws/stocks/stocks.component';
import { LiveSeriesComponent } from './pricing/charts/live-series/live-series.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NavService } from './core/nav/nav.service';
import { StaticDataService } from './common/static-data/static-data.service';
import { EnvService } from './core/nav/env.service';
import { GlobalStateService } from './common/global-state.service';
import { MessageDialogComponent } from './common/message-dialog/message-dialog.component';
import { MessageDialogService } from './common/message-dialog.service';
import { LoginComponent } from './core/login/login.component';
import { JwtModule, JWT_OPTIONS, JwtHelperService } from '@auth0/angular-jwt';
import { DashboardComponent } from './frtb/dashboard/dashboard.component';
import { ImaMrComponent } from './frtb/ima/ima-mr/ima-mr.component';
import { SaMrComponent } from './frtb/sa/sa-mr/sa-mr.component';
import { ExecutionListComponent } from './frtb/exec/execution-list/execution-list.component';
import { AuthInterceptorService } from './core/security/auth-interceptor.service';
import { AgGridModule } from 'ag-grid-angular';
import { AgGridBtnCellRendererComponent } from './common/ag-grid/ag-grid-btn-cell-renderer/ag-grid-btn-cell-renderer.component';
import { BarChartComponent } from './common/ng2-charts/bar-chart/bar-chart.component';
import { SaMrRiskChargeBarChartComponent } from './frtb/sa/sa-mr-risk-charge-bar-chart/sa-mr-risk-charge-bar-chart.component';
import { SaMrSensitivitiesUploadComponent } from './frtb/sa/sa-mr-sensitivities-upload/sa-mr-sensitivities-upload.component';


@NgModule({
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    A11yModule,
    ClipboardModule,
    CdkStepperModule,
    CdkTableModule,
    CdkTreeModule,
    DragDropModule,
    MatAutocompleteModule,
    MatBadgeModule,
    MatBottomSheetModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatCardModule,
    MatCheckboxModule,
    MatChipsModule,
    MatStepperModule,
    MatDatepickerModule,
    MatDialogModule,
    MatDividerModule,
    MatExpansionModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatNativeDateModule,
    MatPaginatorModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatRadioModule,
    MatRippleModule,
    MatSelectModule,
    MatSidenavModule,
    MatSliderModule,
    MatSlideToggleModule,
    MatSnackBarModule,
    MatSortModule,
    MatTableModule,
    MatTabsModule,
    MatToolbarModule,
    MatTooltipModule,
    MatTreeModule,
    OverlayModule,
    PortalModule,
    ScrollingModule,
    NgChartsModule,
    HttpClientModule,
    ReactiveFormsModule,
    JwtModule,
    AgGridModule,
  ],
  declarations: [
    AppComponent,
    PageUnderConstructionComponent,
    SnackBarComponent,
    MenuListItemComponent,
    TopNavComponent,
    LineChartComponent,
    ExpandableTableComponent,
    StocksComponent,
    LiveSeriesComponent,
    MessageDialogComponent,
    LoginComponent,
    DashboardComponent,
    ImaMrComponent,
    SaMrComponent,
    ExecutionListComponent,
    AgGridBtnCellRendererComponent,
    BarChartComponent,
    SaMrRiskChargeBarChartComponent,
    SaMrSensitivitiesUploadComponent,
  ],
  exports: [
    AppComponent,
    PageUnderConstructionComponent,
    SnackBarComponent,
    MenuListItemComponent,
    TopNavComponent,
    LineChartComponent,
    ExpandableTableComponent,
    StocksComponent,
    LiveSeriesComponent,
  ],
  providers: [
    NavService,
    StaticDataService,
    EnvService,
    GlobalStateService,
    MessageDialogService,
    { provide: JWT_OPTIONS, useValue: JWT_OPTIONS },
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true },
    JwtHelperService
  ],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
