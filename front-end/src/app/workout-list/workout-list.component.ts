import { Component, OnInit } from '@angular/core';
import { PostDataService } from 'src/services/post-data.service';

@Component({
  selector: 'app-workout-list',
  templateUrl: './workout-list.component.html',
  styleUrls: ['./workout-list.component.css']
})
export class WorkoutListComponent implements OnInit {
  users: any[] = [];

  constructor(private postDataService: PostDataService) {}

  displayUserComponent = false;

  selectedUsers: any[] = [];

toggleUserComponent(user: any) {
  // Check if the user is already in the selectedUsers array
  const index = this.selectedUsers.findIndex((selectedUser) => selectedUser.username === user.username);

  if (index !== -1) {
    // If the user is already selected, remove them
    this.selectedUsers.splice(index, 1);
  } else {
    // If the user is not selected, add them to the array
    if (user) {
      this.selectedUsers = [user]; // replace the array with a new array containing only user
    }
  }
}
ngOnInit() {
  this.postDataService.getPosts().subscribe((data: any[]) => {
    this.users = data;
  });
}
}
