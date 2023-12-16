import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { AppRoutingModule } from '../app-routing.module';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { WorkoutListComponent } from './workout-list.component';
import { WorkoutComponent } from '../workout/workout.component';
@NgModule({
  declarations: [
    WorkoutListComponent,
    WorkoutComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
  ],
  exports: [
    WorkoutListComponent,
  ]
})
export class WorkoutListModule { }
