import { Component, OnInit } from '@angular/core';
import { PostDataService } from 'src/services/post-data.service';

@Component({
  selector: 'app-workout-list',
  templateUrl: './workout-list.component.html',
  styleUrls: ['./workout-list.component.css']
})
export class WorkoutListComponent implements OnInit {
  workouts: any[] = [];

  constructor(private postDataService: PostDataService) {}

  selectedWorkout: any;

  toggleWorkoutComponent(workout: any) {
    if (this.selectedWorkout === workout) {
      this.selectedWorkout = null;
    } else {
      this.selectedWorkout = workout;
    }
  }

  deleteWorkout(workoutId: string) {
    // Call your delete workout API or service method with workoutId
    // Example: this.postDataService.deleteWorkout(workoutId).subscribe(response => {});
  }

  ngOnInit() {
    this.postDataService.getWorkouts().subscribe((data: any) => {
      this.workouts = data.workouts || [];
    });
  }
}
