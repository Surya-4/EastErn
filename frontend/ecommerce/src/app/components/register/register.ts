import { Component } from '@angular/core';
import { LoginRequest, UserData, UserService } from '../../services/user-service';
import { FormsModule } from "@angular/forms";
import { Router } from '@angular/router';
import { NgIf } from '@angular/common';
import { Toast } from '../../services/toast';

@Component({
  selector: 'app-register',
  imports: [FormsModule,NgIf],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
   loginRequest:LoginRequest={
    userName:'',
    password:''
  };
  userData?:UserData;
  confirmPassword:string='';
  errorMessage:string='';
  constructor(private userservice:UserService,private router:Router,private toast:Toast){}

  onRegister(){
    if(this.loginRequest.password!=this.confirmPassword){
      this.errorMessage="passwords does not match"
      return;
    }
    this.userservice.register(this.loginRequest).subscribe({
      next:(res)=>{
        this.userservice.setUserData(res);
        this.userservice.setLogIn(true);
        this.router.navigate(['/']);
        this.toast.show({
          text:'Registration successful, Welcome to EastErn '+ this.userData?.userName,
          type:'success'
        })
      },
      error:(err)=>{
        this.errorMessage=err.error||'something went wrong';
      }
    })
  }
  
}
