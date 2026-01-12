import { Routes } from '@angular/router';
import { Home } from './components/home/home';
import { Login } from './components/login/login';
import { Profile } from './components/profile/profile';
import { Register } from './components/register/register';
import { Cart } from './components/cart/cart';
import { Payment } from './components/payment/payment';
import { Orders } from './components/orders/orders';

export const routes: Routes = [
    {path:'', component:Home},
    {path:'login',component:Login},
    { path: 'profile', component: Profile },
    {path:'register',component:Register},
    {path:'cart',component:Cart},
    {path:'payment',component:Payment},
    {path:'orders',component:Orders},
    {path:'**',redirectTo:''},
];
