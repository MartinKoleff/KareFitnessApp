import { Component, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';
import { PostDataService } from '../../services/post-data.service';
import { GetUser,  } from '../models/user.model';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../services/auth-service';
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
    private http: HttpClient,
    private authService: AuthService,
    ) {
    this.post.getPosts().subscribe((data) => {
      this.allPosts = data;
    });
  }

  loginCheck() {
    const userCredentials = { email: this.user.email, password: this.user.password };

    this.post.login(userCredentials).subscribe(
      (response: any) => {
        console.log('User logged in successfully:', response);
        this.navigateToMain();  // Optionally navigate to main after successful login
      },
      (error) => {
        console.error('Error logging in:', error);
        alert('Invalid email or password');
      }
    );
  }
  private navigateToMain() {
    this.router.navigate(['/main']);
  }
// loginCheck() {
//   this.authService.setAuthToken('amogus');
//   if (this.check() === false) {
//     alert('Wrong email or password!');
//   } else {
//     // Make an authenticated HTTP request
//     this.http.get('YOUR_API_ENDPOINT', {
//       headers: {
//         Authorization: 'Bearer amogus', // Add the Authorization header with the token
//       },
//     }).subscribe(
//       (response) => {
//         alert('Success');
//         this.router.navigate(['/main']);
//       },
//       (error) => {
//         alert('Error during authentication'); // Handle error appropriately
//       }
//       );
//     }
//   }
}
