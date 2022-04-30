import {Group} from '../group/group.model';

// Model for transferring category.
export class Category {
  id!: number;
  name!: string;
  icon!: string;
  group!: Group;
}
