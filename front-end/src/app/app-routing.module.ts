import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from './home/home.component'
import {LoginComponent} from './login/login.component'
import { RegisterComponent } from './register/register.component';
import { StartupComponent } from './startup/startup.component';
import { MainComponent } from './main/main.component';
import { CreateworkoutComponent } from './createworkout/createworkout.component';
import { BicepsComponent } from './exercise-components/biceps/biceps.component';
import { TricepsComponent } from './exercise-components/triceps/triceps.component';
import { LegsComponent } from './exercise-components/legs/legs.component';
import { BackComponent } from './exercise-components/back/back.component';
import { ChestComponent } from './exercise-components/chest/chest.component';
import { CardioComponent } from './exercise-components/cardio/cardio.component';
import { AbsComponent } from './exercise-components/abs/abs.component';
import { ShouldersComponent } from './exercise-components/shoulders/shoulders.component';

const routes: Routes = [
  {path:'', component:StartupComponent},
  {path:'startup', component: StartupComponent},
  {path:'login', component:LoginComponent},
  {path:'register', component:RegisterComponent},
  {path:'home', component: HomeComponent},
  {path:'main', component: MainComponent},
  {path:'create-workout', component: CreateworkoutComponent},

  {path:'biceps', component: BicepsComponent},
  {path:'triceps', component: TricepsComponent},
  {path:'legs', component: LegsComponent},
  {path:'back', component: BackComponent},
  {path:'shoulders', component: ShouldersComponent},
  {path:'chest', component: ChestComponent},
  {path:'cardio', component: CardioComponent},
  {path:'abs', component: AbsComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {onSameUrlNavigation:'reload'})],
  exports: [RouterModule]
})
export class AppRoutingModule {

}
