import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { NavService } from './nav/nav.service';
import * as globals from 'src/app/globals';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  token:string = '';

  constructor(
    public jwtHelper: JwtHelperService,
    public router: Router,
    public navService: NavService
  ) {}
  // ...
  public isAuthenticated(): boolean {
    if(!globals.enableAuth) {
      return true;
    }
    const token = localStorage.getItem('token');
    // Check whether the token is expired and return
    // true or false
    /*
    if(this.token === '') {
      return false;
    }
    */
    return !this.jwtHelper.isTokenExpired(this.token);
  }

  public setToken(t:string) {
    localStorage.setItem('token', t)
    this.token = t;
  }

  public login() {
    localStorage.removeItem('token');
    this.token = '';
    this.router.navigate(['login']);
  }

  public logout() {
    localStorage.removeItem('token');
    this.token = '';
    this.navService.closeNav();
    this.router.navigate(['login']);
  }

}
