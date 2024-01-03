import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ExerciseSet, Exercises } from 'src/app/models/exercise.model';

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
  workoutWeight: string = '';

  addSet() {
    // Add a set to the selected exercise
    if (this.selectedExercise && this.workoutReps) {
      const newSet: ExerciseSet = {
        reps: parseInt(this.workoutReps),
        weight: parseInt(this.workoutWeight)
      };

      if (!this.selectedExercise.sets) {
        this.selectedExercise.sets = [];
      }

      this.selectedExercise.sets.push(newSet);

      // Clear the reps and weight after adding a set
      this.workoutReps = '';
      this.workoutWeight = '';
    }
    else{
      alert('Please select an exercise and add weight and reps to it');
    }
  }

  editSet(set: ExerciseSet, index: number) {
    // Implement editing logic for a set
    // You may open a modal or use an inline form for editing
    if (this.selectedExercise && this.selectedExercise.sets && this.workoutReps && this.workoutWeight) {
      // Assuming workoutReps and workoutWeight are the updated values from the input boxes
      const updatedSet: ExerciseSet = {
        reps: parseInt(this.workoutReps),
        weight: parseInt(this.workoutWeight)
      };

      // Replace the set at the specified index with the updated set
      this.selectedExercise.sets[index] = updatedSet;

      // Clear the reps and weight after editing
      this.workoutReps = '';
      this.workoutWeight = '';
    }
  }

  deleteSet(set: ExerciseSet) {
    // Remove a set from the selected exercise
    if (this.selectedExercise && this.selectedExercise.sets) {
      this.selectedExercise.sets = this.selectedExercise.sets.filter(s => s !== set);
    }
  }

  selectExercise(exercise: Exercises | null) {
    // Handle null case or emit the event with the selected exercise
    if (exercise) {
      this.selectedExercise = exercise;
      this.exerciseSelected.emit(exercise);
    } else {
      this.selectedExercise = null;
    }
  }

  addExercise() {
    // Add the selected exercise and its reps to the array
    if (this.selectedExercise && this.workoutReps) {
      const newExercise: Exercises = {
        name: this.selectedExercise.name,
        description: this.selectedExercise.description,
        machineType: this.selectedExercise.machineType,
        sets: this.selectedExercise.sets || [],
      };

      // Ensure that 'sets' is defined
      if (!newExercise.sets) {
        newExercise.sets = [];
      }

      // Add the new set
      newExercise.sets.push({
        reps: parseInt(this.workoutReps),
        weight: parseInt(this.workoutWeight),
      });

      this.selectedExercises.push(newExercise);

      // Clear the selected exercise and reps after adding
      this.selectedExercise = null;
      this.workoutReps = '';
      this.workoutSets = '';
    }
  }
}
