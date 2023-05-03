import {Component, ViewChild, ElementRef, ViewEncapsulation, AfterViewInit} from '@angular/core';
import {NavItem} from './core/nav/nav-item';
import {NavService} from './core/nav/nav.service';
import {NotificationService} from './common/notification.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import { MessageDialogComponent } from './common/message-dialog/message-dialog.component';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class AppComponent implements AfterViewInit {
  @ViewChild('appDrawer', { static: true }) public appDrawer!: ElementRef;
  navItems: NavItem[] = [
    {
      displayName: 'Configuration',
      iconName: 'monitoring',
      children: [
        {
          displayName: 'Entity',
          iconName: 'merge',
          route: ''
        },
        {
          displayName: 'Counterparty',
          iconName: 'merge',
          route: ''
        },
      ]
    },
    {
      displayName: 'Static Data',
      iconName: 'currency_exchange',
      route: '',
      children: [
        {
          displayName: 'Agency Spot Prices',
          iconName: 'price_change',
          route: 'pricing/spot-prices'
        },
        {
          displayName: 'Forward Points',
          iconName: 'scoreboard',
          route: 'market-data/forwardPoints',
        },
      ]
    },
    {
      displayName: 'Market Data',
      iconName: 'apartment',
      route: '',
      children: [
        {
          displayName: 'Agency Spot Prices',
          iconName: 'price_change',
          route: ''
        },
        {
          displayName: 'Forward Points',
          iconName: 'scoreboard',
          route: '',
        },
      ]
    },
    {
      displayName: 'IMA - Market Risk',
      iconName: 'bolt',
      route: '',
      children: [
        {
          displayName: 'Expected Shortfall (ES)',
          iconName: 'price_change',
          route: '',
        },
        {
          displayName: 'Non-Modellable Risk Factors (NMRF)',
          iconName: 'price_change',
          route: '',
        },
        {
          displayName: 'Default Risk Charge (DRC)',
          iconName: 'price_change',
          route: '',
        },
      ]
    },
    {
      displayName: 'IMA-XVA',
      iconName: 'insights',
      route: '',
      children: [
        {
          displayName: 'XVA Results',
          iconName: 'price_change',
          route: ''
        },
        {
          displayName: 'Exposure Matrices',
          iconName: 'scoreboard',
          route: '',
        },
      ]
    },
    {
      displayName: 'SA - Market Risk',
      iconName: 'grade',
      route: '',
      children: [
        {
          displayName: 'Sensitivities based Risk Charge ()',
          iconName: 'price_change',
          route: '',
        },
        {
          displayName: 'Default Risk Charge (DRC)',
          iconName: 'price_change',
          route: '',
        },
        {
          displayName: 'Residual Risk Add-On',
          iconName: 'price_change',
          route: '',
        },
      ]
    },
    {
      displayName: 'Test Pages',
      iconName: 'school',
      children: [
        {
          displayName: 'Line Chart',
          iconName: 'stacked_line_chart',
          route: 'samples/charts/line-chart'
        },
        {
          displayName: 'Expandable Table Rows',
          iconName: 'expand_content',
          route: 'samples/ng-examples/expandable-table-rows'
        }
      ]
    }
  ];

  constructor(
    private dialog: MatDialog,
    private navService: NavService, 
    private snackBar: MatSnackBar, 
    private notificationService: NotificationService) {

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
}
