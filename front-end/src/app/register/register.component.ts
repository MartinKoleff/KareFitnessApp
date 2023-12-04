import { Component, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';
import { PostDataService } from '../post-data.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  encapsulation: ViewEncapsulation.None,
})
export class RegisterComponent {
  name: string = 'reader';
  message: string =
    'Welcome to Workfall blog. Start editing to see some magic happen';
  allPosts:any=[];




  username: string = '';
  password: string = '';
  email: string = '';
  cpassword: string = '';
  tempEmail: string = 'admin';
  constructor(private post: PostDataService, private router: Router) {
    this.post.getPosts().subscribe((data) => {
      //console.log(data)
      this.allPosts = data;
    });
  }

  signUpCheck() {
    const uppercaseRegex = /[A-Z]/;
    const numberRegex = /\d/;

    if (this.password == null || this.password.trim() === '') {
      alert('You have to enter a password!');
    } else if (this.email == this.tempEmail) {
      alert('This e-mail is already taken!');
    } else if (this.password != this.cpassword) {
      alert('The password did not match!  ');
    } else if (!uppercaseRegex.test(this.password) || !numberRegex.test(this.password)) {
      alert('This password must contain at least one uppercase letter and one number');
    } else {
      this.router.navigate(['/main']);
    }
  }
}
