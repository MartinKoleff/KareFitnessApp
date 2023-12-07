import { Component } from '@angular/core';
import { Exercises, MuscleGroup, biceps } from '../models/exercise.model';

@Component({
  selector: 'app-createworkout',
  templateUrl: './createworkout.component.html',
  styleUrls: ['./createworkout.component.css']
})
export class CreateworkoutComponent {
  showTricepsExercise: boolean = false;
  showBicepsExercise: boolean = false;
  showLegsExercise: boolean = false;
  showBackExercise: boolean = false;
  showShouldersExercise: boolean = false;
  showChestExercise: boolean = false;
  showCardioExercise: boolean = false;
  showAbsExercise: boolean = false;

  toggleBicepsExercise() {
    this.showBicepsExercise = !this.showBicepsExercise;
  }
  toggleTricepsExercise() {
    this.showTricepsExercise = !this.showTricepsExercise;
  }
  toggleLegsExercise() {
    this.showTricepsExercise = !this.showTricepsExercise;
  }
  toggleBackExercise() {
    this.showBackExercise = !this.showBackExercise;
  }
  toggleShouldersExercise() {
    this.showShouldersExercise = !this.showShouldersExercise;
  }
  toggleChestExercise() {
    this.showChestExercise = !this.showChestExercise;
  }
  toggleCardioExercise() {
    this.showCardioExercise = !this.showCardioExercise;
  }
  toggleAbsExercise() {
    this.showAbsExercise = !this.showAbsExercise;
  }

  selectedExercise: Exercises | null = null;
  bicepsExercises: Exercises[] = [];
  biceps: MuscleGroup = biceps;
  onExerciseSelected(exercise: Exercises) {
    this.selectedExercise = exercise;
  }

  submitWorkout() {
    // Access this.selectedExercise to get the selected exercise
    console.log('Selected Exercise:', this.selectedExercise);
    // Add logic to handle the selected exercise as needed
  }

}
