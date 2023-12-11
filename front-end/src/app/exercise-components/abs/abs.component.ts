import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Exercises } from 'src/app/models/exercise.model';

@Component({
  selector: 'app-abs',
  templateUrl: './abs.component.html',
  styleUrls: ['./abs.component.css']
})
export class AbsComponent {
  @Input() absExercises: Exercises[] = [];
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
    // Check if the selected exercise is already in the array based on the name
    const exerciseExists = this.selectedExercises.some(
      (exercise) => exercise.name === this.selectedExercise?.name
    );
    if(exerciseExists) {
      alert('Exercise already selected.')
    }
    else if (this.selectedExercise && this.workoutReps) {
      // Add the selected exercise and its reps to the array
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
    } else {
      // Handle the case where the exercise already exists in the array
      console.log('Exercise already added or missing information.');
    }
  }
}
