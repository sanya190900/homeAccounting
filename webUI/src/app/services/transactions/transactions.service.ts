import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Period} from '../../models/period/period.model';
import {Transaction} from '../../models/transaction/transaction.model';
import {API_LINKS} from '../../const/api-links';

@Injectable({
  providedIn: 'root'
})
export class TransactionsService {

  constructor(private httpClient: HttpClient) { } // Initialize httpClient for requests

  // Requesting transactions by period and wallet
  getTransactionsByPeriodAndWallet(period: Period, idWallet: number): Observable<any> {
    const params = new HttpParams()
      .set('periodStart', period.start)
      .set('periodStop', period.stop)
      .set('idWallet', idWallet.toString());

    return this.httpClient.get<Transaction[]>(API_LINKS.TRANSACTIONS_URL, { params });
  }

  // Request to add transaction
  addTransaction(transaction: Transaction): Observable<any> {
    return this.httpClient.put(API_LINKS.TRANSACTIONS_URL, transaction);
  }

  // Request to update transaction
  updateTransaction(transaction: Transaction): Observable<any> {
    return this.httpClient.post(API_LINKS.TRANSACTIONS_URL, transaction);
  }

  // Request to delete transaction
  deleteTransactionById(idTransaction: number): Observable<any> {
    return this.httpClient.delete(API_LINKS.TRANSACTIONS_URL + '/' + idTransaction);
  }
}
