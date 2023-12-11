import { Component } from '@angular/core';
import { Exercises, MuscleGroup, Workout, biceps, back, triceps, legs, shoulders, chest, cardio, abs } from '../models/exercise.model';

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
  toggleCardioExercise() {
    this.showCardioExercise = !this.showCardioExercise;
  }
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
  cardioExercises: Exercises[] = [];
  cardio: MuscleGroup = cardio;
  absExercises: Exercises[] = [];
  abs: MuscleGroup = abs;

  isNameProvided: boolean = false;
  selectedExercise: Exercises | null = null;
  selectedExercises: Exercises[] = [];
  workout: Workout = {name: ''};

  onExerciseSelected(exercise: Exercises) {
    this.selectedExercise = exercise;
    if (this.selectedExercises.includes(exercise)) {
      this.selectedExercises = this.selectedExercises.filter(selected => selected !== exercise);
      alert('Exercise removed from selection');
    }
    else if(this.selectedExercises.map(exercise => exercise.reps)==undefined){
      'enter reps please';
    }
    else{
      console.log(this.selectedExercises.map(exercise => exercise.reps));
      this.selectedExercises.push(exercise);
    }
  }
  submitWorkout() {
     if (!this.isNameProvided) {
      alert('Please choose a name for your workout');
    } else if( this.selectedExercises.map(exercise => exercise.reps)==undefined){
      alert('enter reps please')
    }
    else {
      console.log(this.selectedExercises);
    }
  }
  ngOnInit() {
    this.updateSubmitButtonState();
  }
  updateSubmitButtonState() {
    this.isNameProvided = this.workout.name != null && this.workout.name.trim() !== '';
  }
}
