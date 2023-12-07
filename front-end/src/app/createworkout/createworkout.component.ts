import { Component } from '@angular/core';
import { Exercises, MuscleGroup, Workout, biceps, } from '../models/exercise.model';

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

  isNameProvided: boolean = false;
  selectedExercise: Exercises | null = null;
  bicepsExercises: Exercises[] = [];
  selectedExercises: Exercises[] = [];
  biceps: MuscleGroup = biceps;
  workout: Workout = {name: ''};

  onExerciseSelected(exercise: Exercises) {
    this.selectedExercise = exercise;
    if (this.selectedExercises.includes(exercise)) {
      this.selectedExercises = this.selectedExercises.filter(selected => selected !== exercise);
      alert('Exercise removed from selection');
    }
    else{
      this.selectedExercises.push(exercise);
    }
  }
  submitWorkout() {
    if (this.selectedExercises.length === 0) {
      alert('Please select an exercise');
    } else if (!this.isNameProvided) {
      alert('Please choose a name for your workout');
    } else if( this.selectedExercise?.reps==undefined){
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
