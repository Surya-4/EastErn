import { CommonModule, NgFor, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { PaymentService } from '../../services/payment-service';
import { CartInterface } from '../../services/cart-service';

@Component({
  selector: 'app-payment',
  imports: [CommonModule,NgIf,NgFor],
  templateUrl: './payment.html',
  styleUrl: './payment.css'
})
export class Payment {

 paymentMethods = [
    { id: 'UPI', name: 'UPI', icon: '💱' },
    { id: 'CC', name: 'Credit Card', icon: '💳' },
    { id: 'DC', name: 'Debit Card', icon: '💳' },
    { id: 'NB', name: 'Net Banking', icon: '🏦' },
    { id: 'WALLET', name: 'Wallet', icon: '💰' }
  ];
  
  selectedMethod:string='';
  processing=false;
  responseCart:CartInterface={};

  constructor(private router:Router,private paymentService:PaymentService){}

  selectPaymentMethod(method:string){
    this.selectedMethod=method;
  }
  getPaymentMethod(id:string){
    const val=this.paymentMethods.find(p=>p.id===id);
    return val?.name;
  }

 processPayment() {
  this.processing = true;

  this.paymentService.doPayment({ paymentMethod: this.selectedMethod }).subscribe({
    next: (res) => {
      this.responseCart = res;
      this.processing = false;
    },
    error: (err) => {
      this.processing = false;
    }
  });
}

  
  printReceipt() {
    window.print();
  }

  goBack(){
    if(this.responseCart.transactionId){
      this.router.navigate(['/']);
      return;
    }
    this.router.navigate(['/cart']);
  }
}
