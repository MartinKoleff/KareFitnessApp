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
import { WorkoutListComponent } from './workout-list/workout-list.component';
import {AuthGuard} from "../services/auth-guard";
import {OutAuthGuard} from "../services/out-auth-guard";

const routes: Routes = [
  {path:'', component:StartupComponent, canActivate: [AuthGuard]},
  {path:'startup', component: StartupComponent, canActivate: [AuthGuard]},
  { path: 'login', component: LoginComponent, canActivate: [AuthGuard] },
  { path: 'register', component: RegisterComponent, canActivate: [AuthGuard] },
  {path:'home', component: HomeComponent, canActivate:[OutAuthGuard]},
  {path:'main', component: MainComponent, canActivate:[OutAuthGuard]},
  {path:'create-workout', component: CreateworkoutComponent, canActivate: [OutAuthGuard]},
  {path:'workout-list', component: WorkoutListComponent, canActivate: [OutAuthGuard]},

  {path:'biceps', component: BicepsComponent, canActivate: [OutAuthGuard]},
  {path:'triceps', component: TricepsComponent, canActivate: [OutAuthGuard]},
  {path:'legs', component: LegsComponent, canActivate: [OutAuthGuard]},
  {path:'back', component: BackComponent},
  {path:'shoulders', component: ShouldersComponent, canActivate: [OutAuthGuard]},
  {path:'chest', component: ChestComponent, canActivate: [OutAuthGuard]},
  {path:'cardio', component: CardioComponent, canActivate: [OutAuthGuard]},
  {path:'abs', component: AbsComponent, canActivate: [OutAuthGuard]},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { onSameUrlNavigation: 'reload' })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
