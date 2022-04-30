import {Component, Inject, OnInit, Optional, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Transaction} from '../../models/transaction/transaction.model';
import {Category} from '../../models/category/category.model';
import {CategoriesService} from '../../services/categories/categories.service';

@Component({
  selector: 'app-dialog-box',
  templateUrl: './dialog-box-transactions.component.html',
  styleUrls: ['./dialog-box-transactions.component.scss']
})
export class DialogBoxTransactionsComponent implements OnInit {

  action!: string;
  type!: string;
  localData!: any;
  categories!: Category[];

  constructor(
    private categoriesService: CategoriesService,
    public dialogRef: MatDialogRef<DialogBoxTransactionsComponent>,
    @Optional() @Inject(MAT_DIALOG_DATA) public data: Transaction
  ) { }

  ngOnInit(): void {
    this.localData = {...this.data};
    this.action = this.localData.action;

    if (this.action !== 'DELETE') {                                     // Because not needed for DELETE
      if (this.action === 'ADD') {                                      // Because undefined for ADD
        this.localData.category = new Category();
      }
      this.categoriesService.getAllCategories().subscribe(
        categories => {
          this.categories = categories;

          if (this.action === 'ADD') {                                  // Because undefined for ADD due to empty object this.data
            this.localData.category = categories[0];
            this.localData.date = new Date();
            this.localData.value = null;
            this.localData.description = null;
          } else if (this.action === 'UPDATE') {                        // Because needed to find and set chosen category for UPDATE
            this.localData.date = new Date(this.localData.date);
            this.localData.value = Math.abs(this.localData.value);
            for (const category of this.categories) {
              if (category.id === this.localData.category.id) {
                this.localData.category = category;
                break;
              }
            }
          }

          // Choosing right icon for chosen transaction
          if (this.localData.category.group.id === 1) {
            this.type = 'remove';   // - icon
          } else if (this.localData.category.group.id === 2) {
            this.type = 'add';      // + icon
          } else {
            this.type = '';         // default
          }
        },
        error => console.log(error)
      );
    }
  }

  // If category was changed
  onCategoryChange(): void {
    if (this.localData.category.group.id === 1) {
      this.type = 'remove';   // - icon
    } else if (this.localData.category.group.id === 2) {
      this.type = 'add';      // + icon
    } else {
      this.type = '';         // default
    }
  }

  // After click action button (delete, add, update)
  doAction(): void {
    // if it was an expenses category multiply by -1
    if (this.localData.category.group.id === 1) {
      this.localData.value *= -1;
    }

    this.dialogRef.close({event: this.action, data: this.localData}); // Closing dialog box with updated data
  }

  // Closing dialog box
  closeDialog(): void {
    this.dialogRef.close({event: 'Cancel'});
  }

}
