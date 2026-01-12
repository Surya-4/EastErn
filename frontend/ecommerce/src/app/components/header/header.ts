import { Component } from '@angular/core';
import { Login } from '../login/login';
import { CommonModule } from "../../../../node_modules/@angular/common/";
import { UserService } from '../../services/user-service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-header',
  imports: [CommonModule],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {

  constructor(private userservice:UserService){}
  isLoggedIn$!: Observable<boolean>;  
  ngOnInit(){
    this.isLoggedIn$=this.userservice.getLogIn().asObservable();
  }

  logout():void{
    this.menuOpen=!this.menuOpen;
    this.userservice.logout().subscribe({
      next:()=>{
        this.userservice.setUserData(null);
        this.userservice.setLogIn(false);
      },
      error:(err)=>{
        this.userservice.setUserData(null);
        this.userservice.setLogIn(false);
      }
    })
  }
  menuOpen:boolean=false;
  toggleMenu(): void{
      this.menuOpen=!this.menuOpen;
    }

}
