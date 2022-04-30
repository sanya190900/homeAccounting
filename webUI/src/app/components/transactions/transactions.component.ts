import {Component, OnInit, ViewChild} from '@angular/core';
import {MatDatepicker} from '@angular/material/datepicker';
import {PeriodService} from '../../services/period/period.service';
import {Wallet} from '../../models/wallet/wallet.model';
import {FormControl, FormGroup} from '@angular/forms';
import {Period} from '../../models/period/period.model';
import {MatSort} from '@angular/material/sort';
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import {MatDialog} from '@angular/material/dialog';
import {DialogBoxTransactionsComponent} from '../dialog-box-transactions/dialog-box-transactions.component';
import {TransactionsService} from '../../services/transactions/transactions.service';
import {Transaction} from '../../models/transaction/transaction.model';
import {DashboardService} from '../../services/dashboard/dashboard.service';

@Component({
  selector: 'app-transactions',
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.scss']
})
export class TransactionsComponent implements OnInit {

  @ViewChild('picker') datePicker: MatDatepicker<Date> | undefined;   // Find date picker in UI
  @ViewChild(MatPaginator) paginator!: MatPaginator;                  // Find paginator in UI
  @ViewChild(MatSort) sort!: MatSort;                                 // Find sorting in UI

  periods = [                                                         // Definition periods
    {value: PeriodService.getCurrentWeek(), viewValue: 'Week'},
    {value: PeriodService.getCurrentMonth(), viewValue: 'Month'},
    {value: PeriodService.getCurrentYear(), viewValue: 'Year'},
    {value: 'all', viewValue: 'All'},
    {value: 'custom', viewValue: 'Custom'},
  ];
  selectedPeriod = this.periods[1].value;                             // Select default period (current month)

  selectedWallet!: Wallet;
  period = PeriodService.getCurrentMonth();                           // selected period (because of custom and all periods)

  range = new FormGroup({                                     // Default range for date picker (current wick)
    start: new FormControl(new Date(PeriodService.getCurrentWeek().start)),
    end: new FormControl(new Date(PeriodService.getCurrentWeek().stop)),
  });

  dataSource: MatTableDataSource<Transaction> = new MatTableDataSource();   // datasource for table
  displayedColumns: string[] = ['date', 'value', 'category', 'action'];     // Displayed columns in table

  constructor(
    private transactionsService: TransactionsService,   // Initialising transactions service
    private dashboardService: DashboardService,         // Initialising dashboard service
    public dialog: MatDialog                            // Initialising dialogBox
  ) {

  }

  // Getting selected wallet and updating data in charts
  ngOnInit(): void {
    const lsw = localStorage.getItem('wallet');
    this.selectedWallet = lsw !== null ? JSON.parse(lsw) : null;
    this.updateData();
  }

  // Converting date into human-readable format
  parseDate(date: string): string {
    return new Date(date).toLocaleString();
  }

  // Method for opening dialog box
  openDialog(action: string, obj: any): void {
    obj.action = action;
    const dialogRef = this.dialog.open(DialogBoxTransactionsComponent, {
      width: '700px',   // Width of dialog box
      data: obj         // Data transferred to dialog box
    });

    dialogRef.afterClosed().subscribe(      // After close dialog box add update or delete transaction
      result => {
        if (result !== undefined) {
          switch (result.event) {
            case 'ADD':
              this.addTransaction(result.data);
              break;
            case 'UPDATE':
              this.updateTransaction(result.data);
              break;
            case 'DELETE':
              this.deleteTransaction(result.data);
              break;
          }
        }
      }
    );
  }

  // Request to add transaction
  addTransaction(data: any): void {
    const transaction = {
      id: 0,
      date: data.date,
      description: data.description,
      photo: '',
      value: data.value,
      wallet: this.selectedWallet,
      category: data.category
    };

    this.transactionsService.addTransaction(transaction).subscribe(
      () => this.updateData(),  // update data after success adding
      error => console.log(error)
    );
  }

  // Request to update transaction
  updateTransaction(data: any): void {
    const transaction = {
      id: data.id,
      date: data.date,
      description: data.description,
      photo: data.photo,
      value: data.value,
      wallet: this.selectedWallet,
      category: data.category
    };

    this.transactionsService.updateTransaction(transaction).subscribe(
      () => this.updateData(),  // update data after success updating
      error => console.log(error)
    );
  }

  // Request to delete transaction
  deleteTransaction(data: any): void {
    this.transactionsService.deleteTransactionById(data.id).subscribe(
      () => this.updateData(),  // update data after success deleting
      error => console.log(error)
    );
  }

  // Getting data from backend and updating data source for table
  updateData(): void {
    this.transactionsService.getTransactionsByPeriodAndWallet(this.period, this.selectedWallet.id).subscribe(
      transactions => {
        this.dataSource = new MatTableDataSource(transactions);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error => console.log(error)
    );
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
