import { N } from '@angular/cdk/keycodes';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface ProductInterface {
  productId?: string;
  productName?: string;
  price?: number;
  productCategory?: string;
  productBrand?: string;
  imageURL?: string;
  productDescription?: string;
  productColors?: string[];
  materialType?: string;
  selectedColor?:string;
}

@Injectable({
  providedIn: 'root'
})

export class Product {
  private apiUrl='http://localhost:8083/products';

  private productDataSubject=new BehaviorSubject<ProductInterface[]|null>(null);
  products$=this.productDataSubject.asObservable();

  setProducts(products:ProductInterface[]):void{
    this.productDataSubject.next(products);
  }
  constructor(private http:HttpClient){}

  getProducts():Observable<ProductInterface[]> {
    return this.http.get<ProductInterface[]>(this.apiUrl+'/');
  }
}
