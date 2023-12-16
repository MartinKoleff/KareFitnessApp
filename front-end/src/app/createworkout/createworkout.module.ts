import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { AppRoutingModule } from '../app-routing.module';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { CreateworkoutComponent } from './createworkout.component';
import { TricepsComponent } from '../exercise-components/triceps/triceps.component';
import { BicepsComponent } from '../exercise-components/biceps/biceps.component';
import { BackComponent } from '../exercise-components/back/back.component';
import { LegsComponent } from '../exercise-components/legs/legs.component';
import { ShouldersComponent } from '../exercise-components/shoulders/shoulders.component';
import { CardioComponent } from '../exercise-components/cardio/cardio.component';
import { ChestComponent } from '../exercise-components/chest/chest.component';
import { AbsComponent } from '../exercise-components/abs/abs.component';
import { CommonModule } from '@angular/common';
@NgModule({
  declarations: [
    CreateworkoutComponent,
    TricepsComponent,
    BicepsComponent,
    BackComponent,
    LegsComponent,
    ShouldersComponent,
    ChestComponent,
    CardioComponent,
    AbsComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    CommonModule
  ],
  exports: [
    CreateworkoutComponent,
  ]
})
export class CreateworkoutModule { }
