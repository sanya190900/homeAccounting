import {Category} from '../category/category.model';

// Model for transferring amount value in specified category.
export class AmountByCategory {
  category!: Category;
  value!: number;
}
