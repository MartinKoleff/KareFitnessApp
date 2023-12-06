import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from './home/home.component'
import {LoginComponent} from './login/login.component'
import { RegisterComponent } from './register/register.component';
import { StartupComponent } from './startup/startup.component';
import { MainComponent } from './main/main.component';

const routes: Routes = [
  {path:'', component:StartupComponent},
  {path:'startup', component: StartupComponent},
  {path:'login', component:LoginComponent},
  {path:'register', component:RegisterComponent},
  {path:'home', component: HomeComponent},
  {path:'main', component: MainComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {onSameUrlNavigation:'reload'})],
  exports: [RouterModule]
})
export class AppRoutingModule {

}
