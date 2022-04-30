import { Injectable } from '@angular/core';
import {Period} from '../../models/period/period.model';
import {Observable} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {AmountByDate} from '../../models/amount-by-date/amount-by-date.model';
import {API_LINKS} from '../../const/api-links';
import {AmountByCategory} from '../../models/amount-by-category/amount-by-category.model';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(private httpClient: HttpClient) { } // Initialize httpClient for requests

  // Requesting usages by period and wallet
  getUsageByPeriodAndWallet(period: Period, idWallet: number): Observable<any> {
    const params = new HttpParams()
      .set('periodStart', period.start)
      .set('periodStop', period.stop)
      .set('idWallet', idWallet.toString());

    return this.httpClient.get<AmountByDate[]>(API_LINKS.USAGE_BY_DATE_URL, { params });
  }

  // Requesting usages by category group 'expenses' and wallet in period
  getUsageByPeriodAndWalletGroupByCategoryExpenses(period: Period, idWallet: number): Observable<any> {
    const params = new HttpParams()
      .set('periodStart', period.start)
      .set('periodStop', period.stop)
      .set('idWallet', idWallet.toString());

    return this.httpClient.get<AmountByCategory[]>(API_LINKS.USAGE_BY_CATEGORY_EXPENSES_URL, { params });
  }

  // Requesting usages by category group 'income' and wallet in period
  getUsageByPeriodAndWalletGroupByCategoryIncome(period: Period, idWallet: number): Observable<any> {
    const params = new HttpParams()
      .set('periodStart', period.start)
      .set('periodStop', period.stop)
      .set('idWallet', idWallet.toString());

    return this.httpClient.get<AmountByCategory[]>(API_LINKS.USAGE_BY_CATEGORY_INCOME_URL, { params });
  }

  // Requesting date time of first transaction by wallet
  getFirstTransactionDateByWallet(idWallet: number): Observable<any>{
    const params = new HttpParams()
      .set('idWallet', idWallet.toString());

    return this.httpClient.get<AmountByCategory[]>(API_LINKS.FIRST_TRANSACTION_DATE_URL, { params });
  }
}
