import { Component, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';
import { PostDataService } from '../post-data.service';
import { GetUser,  } from '../user.module';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../auth.service';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class LoginComponent {
  // token: string = 'amogus';

  user: GetUser = {password:'',email:''}
  allPosts:any=[];

  constructor(
    private post: PostDataService,
    private router: Router,
    private PostDataService: PostDataService,
    private http: HttpClient,
    private authService: AuthService,
    ) {
    this.post.getPosts().subscribe((data) => {
      this.allPosts = data;
    });
  }

check() {
  for (const userData of this.allPosts) {
    if (this.user.email === userData.email && this.user.password === userData.password) {
      return true;  // Found a match, return true
    }
  }
  return false;  // No match found, return false
}

loginCheck() {
  this.authService.setAuthToken('amogus');
  if (this.check() === false) {
    alert('Wrong email or password!');
  } else {
    // Make an authenticated HTTP request
    this.http.get('YOUR_API_ENDPOINT', {
      headers: {
        Authorization: 'Bearer amogus', // Add the Authorization header with the token
      },
    }).subscribe(
      (response) => {
        alert('Success');
        this.router.navigate(['/main']);
      },
      (error) => {
        alert('Error during authentication'); // Handle error appropriately
      }
      );
    }
  }
}
