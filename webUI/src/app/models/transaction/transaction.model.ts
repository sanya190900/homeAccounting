import {Wallet} from '../wallet/wallet.model';
import {Category} from '../category/category.model';

// Model for transferring transactions.
export class Transaction {
  id?: number;
  date: any;
  description?: string;
  photo?: string;
  value!: number;
  wallet!: Wallet;
  category!: Category;
}
