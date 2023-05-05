import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MessageDialogService } from 'src/app/common/message-dialog.service';
import * as globals from 'src/app/globals';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { NavService } from '../nav/nav.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  constructor(
    private http: HttpClient,
    private dialogService: MessageDialogService,
    private authService: AuthService,
    private router: Router,
    private navService: NavService
  ) { }

  loginForm = new FormGroup({
    username: new FormControl("sigirisetti@gmail.com"),
    password: new FormControl("password")
  });

  onSubmit() {
    let creds = this.loginForm.value;
    if (creds.username === null || creds.password === null || creds.username === '' || creds.password === '') {
      this.dialogService.showErrorMessage('No login detials', 'Please enter username and password');
      return;
    }
    const httpOptions = {
      headers: new HttpHeaders({
        'content-type': 'application/json'
      })
    };
    this.http.post<any>(globals.loginUrl, this.loginForm.value, httpOptions).subscribe({
      next: (res: any) => {
        this.authService.setToken(res.token);
        this.router.navigate(['dashboard']);
        this.navService.openNav();
      },
      error: (e) => console.log(e)
    });
  }
}
