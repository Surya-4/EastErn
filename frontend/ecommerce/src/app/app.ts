import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { UserService } from './services/user-service';
import { Header } from "./components/header/header";
import { CartService } from './services/cart-service';
import { switchMap, tap } from 'rxjs';
import { ToastComponent } from './components/toast-component/toast-component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Header,ToastComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('ecommerce');

  constructor(private userservice:UserService, private cartService:CartService){}
  ngOnInit():void{
    this.userservice.loadUserOnRefresh().pipe(
      tap(res=>{
        this.userservice.setUserData(res);
        this.userservice.setLogIn(true);
        console.log('after refresh',res);
      }),
      switchMap(()=>this.cartService.getUserCart())
    ).subscribe({
      next:res=>{
        this.cartService.setCart(res);
        console.log('cart is ',res);
      },
      error:(err)=>{
        console.log(err);
      }
    })
  }
}
