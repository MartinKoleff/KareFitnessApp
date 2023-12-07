import { Component } from '@angular/core';

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
  submitWorkout(){

  }
}
