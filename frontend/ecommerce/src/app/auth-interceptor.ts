import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { CartService } from './services/cart-service';
import { UserService } from './services/user-service';
import { catchError, throwError } from 'rxjs';
import { ToastrService } from 'ngx-toastr';


export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router=inject(Router);
  const cartService=inject(CartService);
  const userService=inject(UserService);
  const toastr=inject(ToastrService);
  return next(req).pipe(
    catchError(err=>{
      if(err.status==401){
        userService.clearUser();
        cartService.clearCartData();
        router.navigate(['/']);
      }
      return throwError(()=>err);
    })
  );
};
