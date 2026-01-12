import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface cartItem{
  productId?:string|null,
  color?:string|null,
  quantity?:number|null,
  subTotal?:number|null,
}

export interface CartInterface{
  cartId?:string|null,
  userId?:string|null,
  products?:cartItem[]|null,
  amount?:number|null,
  paymentMethod?:string|null,
  transactionId?:string|null,
}

export interface Request{
  productId?:string,
  color?:string,
  delta?:number,
}

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private serviceUrl:string='http://localhost:8080/cart'

  constructor(private http:HttpClient){}

  private cartSubject = new BehaviorSubject<CartInterface>({
    products:[],
  });
  cart$ = this.cartSubject.asObservable();

  setCart(cart: CartInterface) {
    this.cartSubject.next(cart);
  }

  getCart() {
    return this.cartSubject.value;
  }

  clearCartData(){
    this.cartSubject.next({products:[]});
  }
    clearCart():Observable<CartInterface>{
      return this.http.delete<CartInterface>(this.serviceUrl+'/clear',{withCredentials:true});
    }
    
    updateCart(request:Request):Observable<CartInterface>{
      return this.http.put<CartInterface>(this.serviceUrl+'/modifyCart',request,{withCredentials:true});
    }
  
    removeItem(productId:string,color:string):Observable<CartInterface>{
      return this.http.delete<CartInterface>(this.serviceUrl+`/remove/${productId}?color=${color}`,{withCredentials:true});
    }

    getUserCart():Observable<CartInterface>{
      return this.http.get<CartInterface>(this.serviceUrl+'/myCart',{withCredentials:true});
    }
  
    getOrders():Observable<CartInterface[]>{
      return this.http.get<CartInterface[]>(this.serviceUrl+'/orders',{withCredentials:true});
    }
}
