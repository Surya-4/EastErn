import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface UserData {
  userName?: string|null;
  address?: {[key:string]:string} | null;
  email?: string | null;
  mobile?: string | null;
  firstName?: string | null;
  lastName?: string | null;
}

export interface LoginRequest{
  userName:string;
  password:string;
}

@Injectable({
  providedIn: 'root'
})

export class UserService {

  private userDataSubject=new BehaviorSubject<UserData |null>(null);
  userData$=this.userDataSubject.asObservable()
  private serviceUrl='http://localhost:8081/users'
  private logDataSubject=new BehaviorSubject<boolean|false>(false);
  isLoggedIn$=this.logDataSubject.asObservable()

  setLogIn(bool:boolean){
    this.logDataSubject.next(bool)
  }

  clearUser(){
    this.userDataSubject.next(null);
    this.logDataSubject.next(false);
  }
  getLogIn(){
    return this.logDataSubject;
  }
  constructor(private http:HttpClient){}

  login(loginRequest:LoginRequest):Observable<UserData>{

    return this.http.post<UserData>(this.serviceUrl+'/login',loginRequest,{withCredentials:true});
  }

  register(loginRequest:LoginRequest):Observable<UserData>{
    return this.http.post<UserData>(this.serviceUrl+'/register',loginRequest,{withCredentials:true});
  }

  logout():Observable<void>{
    return this.http.post<void>(this.serviceUrl+'/logout',{},{withCredentials:true});
  }
  
  editUser(newData:UserData):Observable<UserData>{
    return this.http.patch<UserData>(this.serviceUrl+'/editUser',newData,{withCredentials:true})
  }
  
  loadUserOnRefresh(){
    return this.http.get<UserData>(this.serviceUrl+'/me',{withCredentials:true});
  }
  setUserData(data:UserData|null){
    this.userDataSubject.next(data);
  }

  getCurrentUser(): UserData | null {
    return this.userDataSubject.value;
  }
}
