import { Component, OnInit } from '@angular/core';
import { Product, ProductInterface } from '../../services/product';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { CartInterface, cartItem, CartService } from '../../services/cart-service';
import { Toast } from '../../services/toast';

@Component({
  selector: 'app-home',
  standalone:true,
  imports: [CommonModule,NgIf,NgFor],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit{

  products: ProductInterface[] =[];

  cart:CartInterface|null=null;

  isCart:boolean=false;

  constructor(private productService:Product,private cartService:CartService,private toast:Toast){}

  ngOnInit(): void {
    this.loadProducts();
    this.cartService.cart$.subscribe(cart=>{
      this.cart=cart;
    });
  }

  increase(product:ProductInterface):void{
    if (!product.selectedColor) {
    this.toast.show({
      text:'please select a color',
      type:'info'
    })
    return;
  }
    this.cartService.updateCart({
      productId:product.productId ,
      color:product.selectedColor,
      delta:1,
    }).subscribe({
      next:(res)=>{
        this.cartService.setCart(res);
      },
      error:(err)=>{
        console.log(err);
        if(err.status===403){
          this.toast.show({
            text:'please login to select products',
            type:'info'
          })
        }
      }
    })
  }

  decrease(product:ProductInterface):void{
    this.cartService.updateCart({
      productId:product.productId ,
      color:product.selectedColor,
      delta:-1,
    }).subscribe({
      next:(res)=>{
        this.cartService.setCart(res);
      },
      error:(err)=>{
        if(err.status===403){
          this.toast.show({
            text:'Please login to choose products',
            type:'info',
          })
        }
      }
    })
  }

  selectColor(product: ProductInterface, color: string): void {
  product.selectedColor = color;
}

  loadProducts(): void{
    this.productService.getProducts().subscribe({
      next:(data)=>{
        this.products=data;
        this.productService.setProducts(data);
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
        if(err.status===403){
          this.toast.show({
            text:'Please login to choose Products',
            type:'info'
          })
        }
      }
    })
  }

  getQuantity(productId:string,color:string):number{
    const item=this.cart?.products?.find(p=> p.productId===productId && p.color===color);
    return item?.quantity ?? 0;
  }
}
