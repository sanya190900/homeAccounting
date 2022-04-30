import {environment} from '../../environments/environment';


export const API_LINKS = {
  // TRANSACTIONS
  TRANSACTIONS_URL: environment.baseUrl + '/transactions',

  // WALLETS
  WALLETS_URL: environment.baseUrl + '/wallets',
  WALLETS_VALUES_URL: environment.baseUrl + '/wallets/values',

  // DASHBOARD
  USAGE_BY_DATE_URL: environment.baseUrl + '/dashboard/usage/byDate',
  USAGE_BY_CATEGORY_EXPENSES_URL: environment.baseUrl + '/dashboard/usage/byCategory/expenses',
  USAGE_BY_CATEGORY_INCOME_URL: environment.baseUrl + '/dashboard/usage/byCategory/income',
  FIRST_TRANSACTION_DATE_URL: environment.baseUrl + '/dashboard/firstTransactionDate',

  // CATEGORIES
  CATEGORIES_URL: environment.baseUrl + '/categories'
};
