import { Component, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class LoginComponent {
  password: string = '';
  email: string = '';
  tempPassword:string = 'admin'
  tempEmail:string = 'admin'
  constructor(private router: Router) {}
  loginCheck(){
    if (this.email!= this.tempEmail && this.password != this.tempPassword){
      alert('Wrong email or password!')
    }
    else{
      this.router.navigate(['/main'])
    }
  }
}
