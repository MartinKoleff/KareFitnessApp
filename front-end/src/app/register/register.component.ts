import { Component, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';
import { PostDataService } from '../post-data.service';
import { PostUser } from '../user.module';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  encapsulation: ViewEncapsulation.None,
})
export class RegisterComponent {
  tempEmail: string = 'admin';
  user: PostUser = {username:'',password:'',email:'',cpassword:''}

  allPosts:any=[];
  constructor(
    private post: PostDataService,
    private router: Router,
    private PostDataService: PostDataService,
    ) {
    this.post.getPosts().subscribe((data) => {
      this.allPosts = data;
    });
  }
  saveUser() {
    this.PostDataService.saveUser(this.user).subscribe(
      (response: any) => {
        console.log('User saved successfully:', response);
      },
      (error) => {
        console.error('Error saving user:', error);
      }
    );
  }

  emailCheck() {
    for (const userData of this.allPosts) {
      if (this.user.email === userData.email) {
        return true;  // Found a match, return true
      }
    }
    return false;  // No match found, return false
  }

  signUpCheck() {
    const uppercaseRegex = /[A-Z]/;
    const numberRegex = /\d/;

    if (this.user.password == null || this.user.password.trim() === '') {
      alert('You have to enter a password!');
    } else if (this.emailCheck()==true) {
      alert('This e-mail is already taken!');
    } else if (this.user.password != this.user.cpassword) {
      alert('The password did not match!  ');
    } else if (!uppercaseRegex.test(this.user.password) || !numberRegex.test(this.user.password)) {
      alert('This password must contain at least one uppercase letter and one number');
    }
    else {
      this.saveUser();
    }
  }
}
