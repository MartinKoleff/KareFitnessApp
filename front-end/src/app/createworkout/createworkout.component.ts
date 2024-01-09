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
  //showCardioExercise: boolean = false;
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
  // toggleCardioExercise() {
  //   this.showCardioExercise = !this.showCardioExercise;
  // }
  toggleAbsExercise() {
    this.showAbsExercise = !this.showAbsExercise;
  }

  bicepsExercises: Exercises[] = [];
  biceps: MuscleGroup = biceps;
  backExercises: Exercises[] = [];
  back : MuscleGroup = back;
  tricepsExercises: Exercises[] = [];
  triceps: MuscleGroup = triceps;
  legsExercises: Exercises[] = [];
  legs: MuscleGroup = legs;
  shouldersExercises: Exercises[] = [];
  shoulders: MuscleGroup = shoulders;
  chestExercises: Exercises[] = [];
  chest: MuscleGroup = chest;
  // cardioExercises: Exercises[] = [];
  // cardio: MuscleGroup = cardio;
  absExercises: Exercises[] = [];
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
    if (!this.isNameProvided) {
      alert('Please choose a name for your workout');
    } else if (this.selectedExercises.every(ex => ex.sets && ex.sets.every(set => set.reps === undefined))) {
      alert('Enter reps, please');
    } else {
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
    }
  }

  ngOnInit() {
    this.updateSubmitButtonState();
  }

  updateSubmitButtonState() {
    this.isNameProvided = this.workout.name != null && this.workout.name.trim() !== '';
  }
}
