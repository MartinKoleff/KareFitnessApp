import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Exercises } from 'src/app/models/exercise.model';

@Component({
  selector: 'app-legs',
  templateUrl: './legs.component.html',
  styleUrls: ['./legs.component.css']
})
export class LegsComponent {
  @Input() legsExercises: Exercises[] = [];
  @Output() exerciseSelected = new EventEmitter<Exercises>();
  @Input() selectedExercises: Exercises[] = [];
  selectedExercise: Exercises | null = null;
  workoutReps: string = '';
  workoutSets: string = '';

  selectExercise(exercise: Exercises | null) {
    // Handle null case or emit the event with the selected exercise
    if (exercise && this.selectedExercise?.reps) {
      this.selectedExercise = exercise;
      this.exerciseSelected.emit(exercise);
    } else {
    }
  }

  addExercise() {
    // Add the selected exercise and its reps to the array
    if (this.selectedExercise && this.workoutReps) {
      this.selectedExercises.push({
        name: this.selectedExercise.name,
        description: this.selectedExercise.description,
        type: this.selectedExercise.type,
        sets: this.workoutSets,
        reps: this.workoutReps
      });

      // Clear the selected exercise and reps after adding
      this.selectedExercise = null;
      this.workoutReps = '';
      this.workoutSets = '';
    }
  }
}
