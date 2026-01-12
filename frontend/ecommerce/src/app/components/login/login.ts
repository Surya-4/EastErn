import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LoginRequest, UserData, UserService } from '../../services/user-service';
import { Router } from '@angular/router';
import { NgIf } from '@angular/common';
import { Header } from '../header/header';
import { switchMap, tap } from 'rxjs';
import { CartService } from '../../services/cart-service';
import { Toast } from '../../services/toast';
@Component({
  selector: 'app-login',
  imports: [FormsModule,NgIf],
  templateUrl: './login.html',
  styleUrl: './login.css',
})

export class Login {

  userData!:UserData;
  loginRequest:LoginRequest={
    userName:'',
    password:''
  };
  errorMessage:string='';

  constructor(private userService:UserService,private router:Router,private cartService:CartService,private toast:Toast){}

  onLogin() {
  this.userService.login(this.loginRequest).pipe(
    tap(res => {
      this.userService.setUserData(res);
      this.userService.setLogIn(true);
    }),
    switchMap(res =>
      this.cartService.getUserCart().pipe(
        tap(cart => {
          this.cartService.setCart(cart);
        }),
        tap(() => {
          this.router.navigate(['/']);
          this.toast.show({
            text: `Welcome Back, ${res.userName}`,
            type: 'success'
          });
        })
      )
    )
  ).subscribe({
    error: () => {
      this.toast.show({
        text: 'Invalid credentials',
        type: 'error'
      });
    }
  });
}
}