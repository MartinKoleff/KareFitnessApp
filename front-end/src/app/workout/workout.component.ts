import { Component, Input } from '@angular/core';
import { PostDataService } from '../../services/post-data.service';
import { Workout } from '../models/exercise.model';

@Component({
  selector: 'app-workout',
  templateUrl: './workout.component.html',
  styleUrls: ['./workout.component.css']
})
export class WorkoutComponent {
  @Input() workout: any = {};

  constructor(private postDataService: PostDataService) {}

  deleteWorkout() {
    const workoutId = this.workout.id; // Assuming 'id' is the property that holds the workout's unique identifier
    this.postDataService.deleteWorkout(workoutId).subscribe(
      () => {
        // Reload the page after successful deletion
        window.location.reload();
      },
      (error) => {
        console.error('Error deleting workout:', error);
        // Handle error (e.g., display an error message to the user)
      }
    );
  }
}
