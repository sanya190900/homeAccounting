import {Component, OnInit} from '@angular/core';
import {WalletsService} from './services/wallets/wallets.service';
import {Wallet} from './models/wallet/wallet.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  wallets?: Wallet[];       // Nullable wallet list
  selectedWallet!: Wallet;  // non-nullable selected wallet

  constructor(private walletsService: WalletsService) { // Initialising wallet service
  }

  // Initializing component.
  ngOnInit(): void {
    this.walletsService.getAllWallets().subscribe(  // non synchronized method for getting all wallets
      wallets => {
        this.wallets = wallets;
        if (localStorage.getItem('wallet') !== null) {                    // If wallet was specified
          const lsw = localStorage.getItem('wallet');                     // getting this wallet from local storage
          this.selectedWallet = lsw !== null ? JSON.parse(lsw) : null;        // If wallet was specified setting it as selected wallet
        }

        this.selectedWallet = wallets[0];                                   // If not specified → selecting default wallet from db
        localStorage.setItem('wallet', JSON.stringify(this.selectedWallet));  // setting it to local storage
      },
      error => console.log(error)   // If error → logging it
    );
  }

  // If wallet was changed
  onWalletChange(): void {
    localStorage.setItem('wallet', JSON.stringify(this.selectedWallet));      // setting it to local storage
  }
}
