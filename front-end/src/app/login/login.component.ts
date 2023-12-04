import { Component, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';
import { PostDataService } from '../post-data.service';
import { GetUser,  } from '../user.module';

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
  constructor(private post: PostDataService, private router: Router, private PostDataService: PostDataService) {
    this.post.getPosts().subscribe((data) => {
      this.allPosts = data;
    });
  }
  // ngOnInit(): void{
  //   this.PostDataService.getAuthenticatedData(this.token).subscribe((data) => {
  //     console.log('authenticated Data:', data)
  //   },
  //   (error)=>{
  //     console.error('Error fetching authenticated data:', error)
  //   });
  // }
check() {
  for (const userData of this.allPosts) {
    if (this.user.email === userData.email && this.user.password === userData.password) {
      return true;  // Found a match, return true
    }
  }
  return false;  // No match found, return false
}
  tempPassword:string = 'admin';
  tempEmail:string = 'admin';

  loginCheck(){
    if (this.check()==false){
      alert('Wrong email or password!')
    }
    else{
      alert('success')
    }
  }
}
