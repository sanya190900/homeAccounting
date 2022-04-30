import {Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {Wallet} from '../../models/wallet/wallet.model';
import {MatTableDataSource} from '@angular/material/table';
import {MatDialog} from '@angular/material/dialog';
import {WalletsService} from '../../services/wallets/wallets.service';
import {DialogBoxWalletsComponent} from '../dialog-box-wallets/dialog-box-wallets.component';

@Component({
  selector: 'app-wallets',
  templateUrl: './wallets.component.html',
  styleUrls: ['./wallets.component.scss']
})
export class WalletsComponent implements OnInit {

  @ViewChild(MatPaginator) paginator!: MatPaginator;  // Find paginator in UI
  @ViewChild(MatSort) sort!: MatSort;                 // Find sorting in UI

  dataSource!: MatTableDataSource<Wallet>;                    // datasource for table
  displayedColumns: string[] = ['name', 'value', 'action'];   // Displayed columns in table

  constructor(
    private walletsService: WalletsService, // Initialising wallet service
    public dialog: MatDialog                // Initialising dialogBox
  ) { }

  // Updating data in charts
  ngOnInit(): void {
    this.updateData();
  }

  // Method for opening dialog box
  openDialog(action: string, obj: any): void {
    obj.action = action;
    const dialogRef = this.dialog.open(DialogBoxWalletsComponent, {
      width: '400px',   // Width of dialog box
      data: obj         // Data transferred to dialog box
    });

    dialogRef.afterClosed().subscribe(       // Getting selected wallet and updating data in charts
      result => {
        if (result !== undefined) {
          switch (result.event) {
            case 'ADD':
              this.addWallet(result.data);
              break;
            case 'UPDATE':
              this.updateWallet(result.data);
              break;
            case 'DELETE':
              this.deleteWallet(result.data);
              break;
          }
        }
      }
    );
  }

  // Request to add transaction
  addWallet(data: any): void {
    const wallet = {
      id: 0,
      name: data.wallet.name,
      currency: data.wallet.currency
    };

    this.walletsService.addWallet(wallet).subscribe(
      () => this.updateData(),
      error => console.log(error)
    );
  }

  // Request to update transaction
  updateWallet(data: any): void {
    const wallet = {
      id: data.wallet.id,
      name: data.wallet.name,
      currency: data.wallet.currency
    };

    this.walletsService.updateWallet(wallet).subscribe(
      () => this.updateData(),
      error => console.log(error)
    );
  }

  // Request to delete transaction
  deleteWallet(data: any): void {
    this.walletsService.deleteWallet(data.wallet.id).subscribe(
      () => this.updateData(),
      error => console.log(error)
    );
  }

  // Getting data from backend and updating data source for table
  updateData(): void {
    this.walletsService.getWalletsAndCurrentValues().subscribe(
      wallets => {
        this.dataSource = new MatTableDataSource(wallets);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error => console.log(error)
    );
  }

}
