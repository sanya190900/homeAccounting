import {Component, OnInit, ViewChild} from '@angular/core';
import {PeriodService} from '../../services/period/period.service';
import {MatDatepicker} from '@angular/material/datepicker';
import {Period} from '../../models/period/period.model';
import {FormControl, FormGroup} from '@angular/forms';
import {TransactionsService} from '../../services/transactions/transactions.service';
import {Wallet} from '../../models/wallet/wallet.model';
import {DashboardService} from '../../services/dashboard/dashboard.service';
import {AmountByDate} from '../../models/amount-by-date/amount-by-date.model';

// Imports for exporting charts from UI.
import * as Highcharts from 'highcharts';

declare var require: any;
const More = require('highcharts/highcharts-more');
More(Highcharts);

import Histogram from 'highcharts/modules/histogram-bellcurve';
Histogram(Highcharts);

import highcharts3D from 'highcharts/highcharts-3d';
import {AmountByCategory} from '../../models/amount-by-category/amount-by-category.model';
highcharts3D(Highcharts);

const Exporting = require('highcharts/modules/exporting');
Exporting(Highcharts);

const ExportData = require('highcharts/modules/export-data');
ExportData(Highcharts);

const Accessibility = require('highcharts/modules/accessibility');
Accessibility(Highcharts);

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  @ViewChild('picker') datePicker: MatDatepicker<Date> | undefined;         // Find date picker in UI

  periods = [                                                               // Definition periods
    {value: PeriodService.getCurrentWeek(), viewValue: 'Week'},
    {value: PeriodService.getCurrentMonth(), viewValue: 'Month'},
    {value: PeriodService.getCurrentYear(), viewValue: 'Year'},
    {value: 'all', viewValue: 'All'},
    {value: 'custom', viewValue: 'Custom'},
  ];
  selectedPeriod = this.periods[1].value;                                   // Select default period (current month)

  highcharts: typeof Highcharts = Highcharts;                               // Define variable for working with charts
  chartOptionsAreaSpline!: {};                                              // Area spline config
  chartOptionsPieExpenses!: {};                                             // Pie config (expenses)
  chartOptionsPieIncome!: {};                                               // Pie config (incomes)

  selectedWallet!: Wallet;
  period = PeriodService.getCurrentMonth();                                 // selected period (because of custom and all periods)

  range = new FormGroup({                                           // Default range for date picker (current wick)
    start: new FormControl(new Date(PeriodService.getCurrentWeek().start)),
    end: new FormControl(new Date(PeriodService.getCurrentWeek().stop)),
  });

  data: number[][] = [];                                                    // data array for charts

  constructor(
    private transactionsService: TransactionsService,                       // Initialising transactions service
    private dashboardService: DashboardService                              // Initialising dashboard service
  ) { }

  // Getting selected wallet and updating data in charts
  ngOnInit(): void {
    const lsw = localStorage.getItem('wallet');
    this.selectedWallet = lsw !== null ? JSON.parse(lsw) : null;
    this.updateData();
  }

  // If period was changed
  onPeriodChange(): void {
    if (this.selectedPeriod instanceof Period) {      // If it is Period instance
      this.period = this.selectedPeriod;              // selecting period
      this.updateData();                              // updating data for selecting period
    } else if (this.selectedPeriod === 'custom') {    // If it is custom period
      this.datePicker?.open();                        // Opening datepicker
    } else {                                          // If it is all period
      this.getAllData();                              // Getting all data
    }
  }

  // Method for get all transactions data
  getAllData(): void {
    this.dashboardService.getFirstTransactionDateByWallet(this.selectedWallet.id).subscribe(
      datetime => {
        this.period = PeriodService.createPeriod(
          new Date(datetime),
          new Date()
        );

        this.updateData();
      },
      error => console.log(error)
    );
  }

  // Method for creating custom period
  createCustomPeriod(): void {
    if (this.range.value.end !== null) {
      this.period = PeriodService.createPeriod(
        new Date(this.range.value.start.getTime() - new Date().getTimezoneOffset() * 60 * 1000),  // Converting to our time zone
        new Date(this.range.value.end.getTime() - new Date().getTimezoneOffset() * 60 * 1000)     // Converting to our time zone
      );
      this.updateData();
    }
  }

  // Updating data in charts
  updateData(): void {
    this.dashboardService.getUsageByPeriodAndWallet(this.period, this.selectedWallet.id).subscribe(
      usage => {

        usage.forEach((u: AmountByDate) => {
            this.data.push([new Date(u.datetime).getTime(), u.value]);  // Converting usage to data type
          });

        this.chartOptionsAreaSpline = {   // Setting up usage chart
          chart: { type: 'areaspline' },
          title: { text: 'Usage overview' },
          xAxis: {
            type: 'datetime',
            min: new Date(this.period.start).getTime(),
            max: new Date(this.period.stop).getTime()
          },
          yAxis: {
            title: { text: this.selectedWallet.currency }
          },
          tooltip: {
            valueSuffix: ' ' + this.selectedWallet.currency,
            xDateFormat: '%A, %b %e'
          },
          legend: {
            enabled: false
          },
          plotOptions: {
            areaspline: {
              fillOpacity: 0.5,
              marker: { enabled: false }
            }
          },
          series: [{
            name: this.selectedWallet.currency,
            data: this.data
          }]
        };
      },
      error => console.log(error)
    );

    this.dashboardService.getUsageByPeriodAndWalletGroupByCategoryExpenses(this.period, this.selectedWallet.id).subscribe(
      usage => {
        const dataExpenses: any[][] = [];

        usage.forEach((u: AmountByCategory) => {
            dataExpenses.push([u.category.name, u.value]);  // Converting usage to data type
        });

        this.chartOptionsPieExpenses = {  // Setting up pie chart
          chart: {
            type: 'pie',
            options3d: {
              enabled: true,
              alpha: 45
            }
          },
          title: { text: 'Expenses overview by categories' },
          plotOptions: {
            pie: {
              innerSize: 100,
              depth: 45
            }
          },
          series: [{
            name: this.selectedWallet.currency,
            data: dataExpenses
          }]
        };
      },
      error => console.log(error)
    );

    this.dashboardService.getUsageByPeriodAndWalletGroupByCategoryIncome(this.period, this.selectedWallet.id).subscribe(
      usage => {
        const dataIncome: any[][] = [];

        usage.forEach((u: AmountByCategory) => {
          dataIncome.push([u.category.name, u.value]);  // Converting usage to data type
        });

        this.chartOptionsPieIncome = {  // Setting up pie chart
          chart: {
            type: 'pie',
            options3d: {
              enabled: true,
              alpha: 45
            }
          },
          title: { text: 'Income overview by categories' },
          plotOptions: {
            pie: {
              innerSize: 100,
              depth: 45
            }
          },
          series: [{
            name: this.selectedWallet.currency,
            data: dataIncome
          }]
        };
      },
      error => console.log(error)
    );
  }

  // Method for hiding datepicker if needed
  isHidden(): null | { visibility: 'hidden'; width: 0 } {
    if (this.selectedPeriod !== 'custom') {
      return {
        width: 0,
        visibility: 'hidden'
      };
    }

    return null;
  }

  // Method for cancelling selecting custom period
  cancelCustomPeriod(): void {
    this.selectedPeriod = this.periods[1].value;
    this.onPeriodChange();
  }
}
