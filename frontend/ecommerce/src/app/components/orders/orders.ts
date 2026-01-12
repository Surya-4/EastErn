import { Component } from '@angular/core';
import { CartInterface, CartService } from '../../services/cart-service';
import { NgFor, NgIf } from '@angular/common';
import { Product, ProductInterface } from '../../services/product';

@Component({
  selector: 'app-orders',
  imports: [NgIf,NgFor],
  templateUrl: './orders.html',
  styleUrl: './orders.css'
})
export class Orders {
  orders:CartInterface[]=[];
  products:ProductInterface[]=[];
  isLoading:boolean=false;
  errorMessage=''

  paymentMethods = [
    { id: 'UPI', name: 'UPI', icon: '💱' },
    { id: 'CC', name: 'Credit Card', icon: '💳' },
    { id: 'DC', name: 'Debit Card', icon: '💳' },
    { id: 'NB', name: 'Net Banking', icon: '🏦' },
    { id: 'WALLET', name: 'Wallet', icon: '💰' }
  ];

  getPaymentMethod(method:string){
    const res=this.paymentMethods.find(p=>p.id===method);
    return res?.name;
  }

  getProduct(id:string){
    return this.products.find(p=>p.productId===id);
  }

  printReceipt(){
    window.print();
  }
  constructor(private cartService:CartService,private productService:Product){}

  ngOnInit():void{
    this.fetchOrders();
    this.productService.getProducts().subscribe({
      next:res=>{
        this.products=res;
      },
      error(err) {
        console.log(err);
      },
    })
  }

  fetchOrders(){
    this.cartService.getOrders().subscribe({
      next:(res)=>{
        this.orders=res;
      },
      error(err) {
        console.log(err);
      },
    })
  }
}
