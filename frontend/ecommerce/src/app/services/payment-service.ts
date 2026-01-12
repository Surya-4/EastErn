import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { CartInterface } from './cart-service';

export interface PaymentInterface{
paymentMethod:string,
}

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
   private serviceUrl='http://localhost:8084/payments';

  constructor(private http:HttpClient){}
  doPayment(payment:PaymentInterface):Observable<CartInterface>{
    return this.http.post<CartInterface>(this.serviceUrl+'/pay',payment,{withCredentials:true});
  }
}
