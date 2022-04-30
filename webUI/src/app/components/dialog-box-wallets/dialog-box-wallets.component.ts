import {Component, Inject, OnInit, Optional} from '@angular/core';
import {Wallet} from '../../models/wallet/wallet.model';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Transaction} from '../../models/transaction/transaction.model';

@Component({
  selector: 'app-dialog-box-wallets',
  templateUrl: './dialog-box-wallets.component.html',
  styleUrls: ['./dialog-box-wallets.component.scss']
})
export class DialogBoxWalletsComponent implements OnInit {

  action!: string;
  localData!: any;
  wallets!: Wallet[];

  constructor(
    public dialogRef: MatDialogRef<DialogBoxWalletsComponent>,
    @Optional() @Inject(MAT_DIALOG_DATA) public data: Transaction
  ) { }

  ngOnInit(): void {
    this.localData = {...this.data};
    this.action = this.localData.action;

    if (this.action !== 'DELETE') {   // Because not needed for DELETE
      if (this.action === 'ADD') {    // Because undefined for ADD
        this.localData.wallet = new Wallet();
      }
    }
  }

  // After click action button (delete, add, update)
  doAction(): void {
    this.dialogRef.close({event: this.action, data: this.localData}); // Closing dialog box with updated data
  }

  // Closing dialog box
  closeDialog(): void {
    this.dialogRef.close({event: 'Cancel'});
  }

}
