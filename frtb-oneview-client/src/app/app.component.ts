import { Component, ViewChild, ElementRef, ViewEncapsulation, AfterViewInit } from '@angular/core';
import { NavItem } from './core/nav/nav-item';
import { NavService } from './core/nav/nav.service';
import { NotificationService } from './common/notification.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog, MatDialogConfig } from "@angular/material/dialog";
import { MessageDialogComponent } from './common/message-dialog/message-dialog.component';
import { AuthService } from './core/auth.service';


@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class AppComponent implements AfterViewInit {

  title = "FRTB Oneview"

  @ViewChild('appDrawer', { static: true }) public appDrawer!: ElementRef;

  navItems: NavItem[] = [
    {
      displayName: 'Dashboard',
      iconName: 'dashboard',
      route: 'dashboard'
    },
    {
      displayName: 'Final Capital Charge',
      iconName: 'functions',
      children: [
        {
          displayName: 'Overall',
          iconName: 'functions',
          route: 'rc/overall',
        },
        {
          displayName: 'Location Wise',
          iconName: 'functions',
          route: 'rc/deskWise',
        },
        {
          displayName: 'Desk Wise',
          iconName: 'functions',
          route: 'rc/deskWise',
        },
        {
          displayName: 'Asset Class Wise',
          iconName: 'functions',
          route: 'rc/deskWise',
        },
        {
          displayName: 'Top N Trades',
          iconName: 'functions',
          route: 'sa/mr/topNTrades',
        },
      ]
    },
    {
      displayName: 'IMA - Expected Shortfall (ES)',
      iconName: 'bolt',
      children: [
        {
          displayName: 'Modellable Risk Factors (MRF)',
          iconName: 'bolt',
          route: 'ima/mr/mrf',
        },
      ]
    },
    {
      displayName: 'IMA - Default Risk Charge (DRC)',
      iconName: 'bolt',
      children: [
        {
          displayName: 'Regression Details',
          iconName: 'bolt',
          route: 'ima/mr/drc/reg',
        },
        {
          displayName: 'Index and Sector Correlation Matrix',
          iconName: 'bolt',
          route: 'ima/mr/drc/idSecCorr',
        },
        {
          displayName: 'Calibration Summary',
          iconName: 'bolt',
          route: 'ima/mr/drc/calibSummary',
        },
      ]
    },
    {
      displayName: 'IMA - NMRF',
      iconName: 'bolt',
      children: [
        {
          displayName: 'Non-Modellable RFs (NMRF)',
          iconName: 'bolt',
          route: 'ima/mr/nmrf',
        },
      ]
    },
    {
      displayName: 'IMA-XVA',
      iconName: 'bolt',
      children: [
        {
          displayName: 'XVA Results',
          iconName: 'bolt',
          route: 'ima/xva/xva'
        },
        {
          displayName: 'Exposure Matrices',
          iconName: 'bolt',
          route: 'ima/xva/exposureMatrices',
        },
      ]
    },
    {
      displayName: 'SA - MR',
      iconName: 'grade',
      children: [
        {
          displayName: 'SA - MR (All)',
          iconName: 'bolt',
          route: 'sa/mr/all',
        },
        {
          displayName: 'Sensitivities Upload',
          iconName: 'bolt',
          route: 'sa/mr/sensitivities-upload',
        },
        {
          displayName: 'What-If Analysis',
          iconName: 'bolt',
          route: 'sa/mr/whatIf',
        },
        {
          displayName: 'Incremental VaR',
          iconName: 'bolt',
          route: 'sa/mr/ivar',
        },
        {
          displayName: 'Incremental VaR',
          iconName: 'bolt',
          route: 'sa/mr/ivar',
        },
      ]
    },
    {
      displayName: 'SA - CVA',
      iconName: 'grade',
      children: [
        {
          displayName: 'CVA Aggregation',
          iconName: 'grade',
          route: 'sa/cva',
        },
      ]
    },
    {
      displayName: 'SA - CCR',
      iconName: 'grade',
      children: [
        {
          displayName: 'CCR Results',
          iconName: 'grade',
          route: 'sa/ccr',
        },
      ]
    },
    /*
    {
      displayName: 'Market Data',
      iconName: 'apartment',
      route: '',
      children: [
        {
          displayName: 'Quotes',
          iconName: 'price_change',
          route: 'md/quotes'
        }
      ]
    },
    {
      displayName: 'Static Data',
      iconName: 'currency_exchange',
      children: [
        {
          displayName: 'SA-MR',
          iconName: 'currency_exchange',
          children: [
            {
              displayName: 'Equity Bucket Mapping',
              iconName: 'price_change',
              route: 'sd/samr/eqBucketMapping'
            },
            {
              displayName: 'Correlation Parameter',
              iconName: 'scoreboard',
              route: 'sd/samr/correlParams',
            },
            {
              displayName: 'Risk Weights',
              iconName: 'scoreboard',
              route: 'sd/samr/riskWeights',
            },
            {
              displayName: 'Credit Non-Sec Bucketing',
              iconName: 'scoreboard',
              route: 'sd/samr/crNonSecBucketing',
            },
            {
              displayName: 'Credit Sec Bucketing',
              iconName: 'scoreboard',
              route: 'sd/samr/crSecBucketing',
            },
            {
              displayName: 'Credit Sec Bucketing (DRC)',
              iconName: 'scoreboard',
              route: 'sd/samr/crNonSecBucketingDRC',
            },
            {
              displayName: 'Credit Sec CTP Bucketing (DRC)',
              iconName: 'scoreboard',
              route: 'sd/samr/crSecBucketingDRC',
            },
          ]
        }

      ]
    },
    {
      displayName: 'Configuration',
      iconName: 'monitoring',
      children: [
        {
          displayName: 'Entity',
          iconName: 'merge',
          route: 'config/entity'
        },
        {
          displayName: 'Counterparty',
          iconName: 'merge',
          route: 'config/entity'
        },
      ]
    },
    */
    {
      displayName: 'Test Pages',
      iconName: 'school',
      children: [
        {
          displayName: 'Angular Components',
          iconName: 'school',
          children: [
            {
              displayName: 'Expandable Table Rows',
              iconName: 'school',
              route: 'samples/ng-examples/expandable-table-rows'
            }
          ]
        },
        {
          displayName: 'Ng2-Charts',
          iconName: 'school',
          children: [
            {
              displayName: 'Ng2 Line Chart',
              iconName: 'school',
              route: 'samples/ng2-charts/line-chart'
            }
          ]
        },
        {
          displayName: 'D3-Charts',
          iconName: 'school',
          children: [
            {
              displayName: 'D3 Bar Chart',
              iconName: 'school',
              route: 'samples/d3-charts/bar-chart'
            },
            {
              displayName: 'D3 Pie Chart',
              iconName: 'school',
              route: 'samples/d3-charts/pie-chart'
            },
            {
              displayName: 'D3 Scatter Chart',
              iconName: 'school',
              route: 'samples/d3-charts/scatter-chart'
            },
          ]
        },
      ]
    },
  ];

  constructor(
    private dialog: MatDialog,
    private navService: NavService,
    private snackBar: MatSnackBar,
    private notificationService: NotificationService,
    private authService: AuthService) {

    this.notificationService.notification$.subscribe(message => {
      this.snackBar.open(message, "Close", {
        duration: 3000,
        panelClass: ['blue-snackbar']
      });
    });
    this.notificationService.error$.subscribe(message => {
      this.snackBar.open(message, "Close", {
        duration: 3000,
        panelClass: ['red-snackbar']
      });
    });
  }

  openDialog() {

    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;

    this.dialog.open(MessageDialogComponent, dialogConfig);
  }

  ngAfterViewInit() {
    this.navService.appDrawer = this.appDrawer;
  }

  public logout(): void {
    // todo
  }

  public isAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }
}
