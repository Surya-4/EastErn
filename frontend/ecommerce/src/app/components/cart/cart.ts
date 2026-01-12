import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { CartInterface, CartService } from '../../services/cart-service';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { Observable } from 'rxjs';
import { Product, ProductInterface } from '../../services/product';
import { Home } from '../home/home';
import { ToastrService } from 'ngx-toastr';
import { Toast } from '../../services/toast';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cart',
  imports: [CommonModule],
  templateUrl: './cart.html',
  styleUrl: './cart.css',
})
export class Cart {

  cart:CartInterface={
    products:[]
  };

  constructor(private cartService:CartService,private productService:Product,private toast:Toast,private router:Router){}

  cart$!:Observable<CartInterface>;
  products!:ProductInterface[]|null;
  productMap = new Map<string, ProductInterface>();

  ngOnInit():void{
  this.cart$=this.cartService.cart$;

  this.productService.getProducts().subscribe(products => {
    this.productMap.clear();
    products!.forEach(p => {
      if (p.productId) {
        this.productMap.set(p.productId, p);
      }
    });
  });
  }

    increase(productId:string,color:string):void{
      this.cartService.updateCart({
        productId:productId ,
        color:color,
        delta:1,
      }).subscribe({
        next:(res)=>{
          this.cartService.setCart(res);
        },
        error:(err)=>{
          console.log(err);
        }
      })
    }
  
    decrease(productId:string,color:string):void{
      this.cartService.updateCart({
        productId:productId ,
        color:color,
        delta:-1,
      }).subscribe({
        next:(res)=>{
          this.cartService.setCart(res);
        },
        error:(err)=>{
          console.log(err);
        }
      })
    }
   delete(productId?:string,color?:string):void{
    if(!productId || !color){
      return;
    }
    this.cartService.removeItem(productId,color).subscribe({
      next:(res)=>{
        this.cartService.setCart(res);
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }

  checkout(): void {
    this.toast.show({
      text: 'Do you want to proceed to checkout?',
      type: 'info',
      action: {
        text: 'OK',
        callback: () => {
          this.router.navigate(['/payment']);
        }
      },
      cancelAction: {
        text: 'Cancel',
        callback: () => {
          
        }
      }
    });
  }

  clearCart():void{
    this.toast.show({
      text:'Do you really want to clear cart',
      type:'info',
      action:{
        text:'Yes',
        callback:()=> {
          this.cartService.clearCart().subscribe({
            next:(res)=>{
              this.cartService.setCart(res),
              this.toast.show({
                text:'Cart Cleared Succesfully',
                type:'success',
              })
            }
          })
        }
      },
      cancelAction:{
          text:'cancel',
          callback:()=>{

          }
        }
    })
  }
}
