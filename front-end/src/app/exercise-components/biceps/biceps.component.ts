import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Exercises } from 'src/app/models/exercise.model';

@Component({
  selector: 'app-biceps',
  templateUrl: './biceps.component.html',
  styleUrls: ['./biceps.component.css']
})
export class BicepsComponent {
  @Input() bicepsExercises: Exercises[] = [];
  @Output() exerciseSelected = new EventEmitter<Exercises>();

  selectedExercise: Exercises | null = null;

  selectExercise(exercise: Exercises | null) {
    // Handle null case or emit the event with the selected exercise
    if (exercise) {
      this.selectedExercise = exercise;
      this.exerciseSelected.emit(exercise);
    } else {
      // Handle null case, e.g., clear the selection or show an error message
    }
  }
}
