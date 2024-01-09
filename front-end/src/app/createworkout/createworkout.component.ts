import { Component } from '@angular/core';
import { Exercises, MuscleGroup, Workout, biceps, back, triceps, legs, shoulders, chest, abs } from '../models/exercise.model';
import {PostDataService} from "../../services/post-data.service";
import {Router} from "@angular/router";

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
  showAbsExercise: boolean = false;

  constructor(private postDataService: PostDataService, private router : Router) {}
  toggleBicepsExercise() {
    this.showBicepsExercise = !this.showBicepsExercise;
  }
  toggleTricepsExercise() {
    this.showTricepsExercise = !this.showTricepsExercise;
  }
  toggleLegsExercise() {
    this.showLegsExercise = !this.showLegsExercise;
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
  toggleAbsExercise() {
    this.showAbsExercise = !this.showAbsExercise;
  }

  biceps: MuscleGroup = biceps;
  back : MuscleGroup = back;
  triceps: MuscleGroup = triceps;
  legs: MuscleGroup = legs;
  shoulders: MuscleGroup = shoulders;
  chest: MuscleGroup = chest;
  abs: MuscleGroup = abs;

  isNameProvided: boolean = false;
  selectedExercise: Exercises | null = null;
  selectedExercises: Exercises[] = [];
  workout: Workout = {name: '', exercises: [] =[]};

  onExerciseSelected(exercise: Exercises) {
    this.selectedExercise = exercise;

    // Check if the exercise is already in the selectedExercises array
    const existingExerciseIndex = this.selectedExercises.findIndex(
      (selected) => selected.name === exercise.name
    );

    if (existingExerciseIndex !== -1) {
      // Remove the exercise if it's already in the list and add the new one with the new set
      this.selectedExercises.splice(existingExerciseIndex, 1);
      this.selectedExercises.push(exercise);
    } else if (exercise.sets && exercise.sets.every(set => set.reps === undefined)) {
      // Display an alert if all sets have undefined reps
      alert('Enter reps, please');
    } else {
      // Add the exercise to the selectedExercises array
      this.selectedExercises.push(exercise);
    }
  }

  submitWorkout() {
    // Remove exercises with invalid sets
    this.selectedExercises = this.selectedExercises.filter(ex => ex.sets != null && ex.sets.length > 0);

    // Check if there are still exercises left
    if (this.selectedExercises.length > 0) {
      // Create a Workout object
      const submittedWorkout: Workout = {
        name: this.workout.name,
        description: this.workout.description || '',
        exercises: this.selectedExercises,
      };

      // Use the PostDataService to submit the workout
      this.postDataService.submitWorkout(submittedWorkout).subscribe(
        (response) => {
          console.log('Successfully submitted workout:', response);
          this.router.navigate(['/workout-list']);
        },
        (error) => {
          console.error('Error submitting workout:', error);
        }
      );
    } else {
      alert('Please make sure at least one exercise has valid sets');
    }
  }
  updateSubmitButtonState() {
    this.isNameProvided = this.workout.name != null && this.workout.name.trim() !== '';
  }
}
