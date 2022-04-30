import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {API_LINKS} from '../../const/api-links';
import {Category} from '../../models/category/category.model';

@Injectable({
  providedIn: 'root'
})
export class CategoriesService {

  constructor(private httpClient: HttpClient) { } // Initialize httpClient for requests

  // Requesting all categories
  getAllCategories(): Observable<any> {
    return this.httpClient.get<Category[]>(API_LINKS.CATEGORIES_URL);
  }
}
