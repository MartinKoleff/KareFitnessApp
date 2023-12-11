import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Exercises, cardio } from '../../models/exercise.model';

@Component({
  selector: 'app-cardio',
  templateUrl: './cardio.component.html',
  styleUrls: ['./cardio.component.css']
})
export class CardioComponent {
  @Input() cardioExercises: Exercises[] = [];
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
