import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Wallet } from '../../models/wallet/wallet.model';
import { API_LINKS } from '../../const/api-links';
import { WalletAndValue } from '../../models/wallet/wallet-and-value.model';

@Injectable({
  providedIn: 'root'
})
export class WalletsService {

  constructor(private httpClient: HttpClient) { } // Initialize httpClient for requests

  // Requesting all wallets
  getAllWallets(): Observable<any> {
    return this.httpClient.get<Wallet[]>(API_LINKS.WALLETS_URL);
  }

  // Requesting all wallets and value
  getWalletsAndCurrentValues(): Observable<any> {
    return this.httpClient.get<WalletAndValue[]>(API_LINKS.WALLETS_VALUES_URL);
  }

  // Request to add wallet
  addWallet(wallet: Wallet): Observable<any> {
    return this.httpClient.put(API_LINKS.WALLETS_URL, wallet);
  }

  // Request to update wallet
  updateWallet(wallet: Wallet): Observable<any> {
    return this.httpClient.post(API_LINKS.WALLETS_URL, wallet);
  }

  // Request to delete wallet
  deleteWallet(idWallet: number): Observable<any> {
    return this.httpClient.delete(API_LINKS.WALLETS_URL + '/' + idWallet);
  }
}
