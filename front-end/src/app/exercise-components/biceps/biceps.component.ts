import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Exercises } from 'src/app/models/exercise.model';
import { biceps } from '../../models/exercise.model';

@Component({
  selector: 'app-biceps',
  templateUrl: './biceps.component.html',
  styleUrls: ['./biceps.component.css']
})
export class BicepsComponent {
  @Input() bicepsExercises: Exercises[] = [];
  @Output() exerciseSelected = new EventEmitter<Exercises>();
  @Input() selectedExercises: Exercises[] = [];
  selectedExercise: Exercises | null = null;
  workoutReps: string = '';

  selectExercise(exercise: Exercises | null) {
    // Handle null case or emit the event with the selected exercise
    if (exercise && this.workoutReps) {
      this.selectedExercise = exercise;
      this.exerciseSelected.emit(exercise);
    } else {
      // Handle null case, e.g., clear the selection or show an error message
    }
  }

  addExercise() {
    // Add the selected exercise and its reps to the array
    if (this.selectedExercise && this.workoutReps) {
      this.selectedExercises.push({
        name: this.selectedExercise.name, // Assuming 'name' is a property of Exercises
        description: this.selectedExercise.description,
        type: this.selectedExercise.type,
        sets: this.selectedExercise.sets,
        reps: this.workoutReps
      });

      // Clear the selected exercise and reps after adding
      this.selectedExercise = null;
      this.workoutReps = '';
    }
  }
}
