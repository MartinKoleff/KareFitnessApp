import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import{HttpClientModule} from '@angular/common/http';
import { PostDataService } from '../services/post-data.service';
import { LoginModule } from './login/login.module';
import { RegisterModule } from './register/register.module';
import { MainModule } from './main/main.module';
import { StartupModule } from './startup/startup.module';
import { HomeModule } from './home/home.module';
import { CreateworkoutModule } from './createworkout/createworkout.module';
import { WorkoutListModule } from './workout-list/workout-list.module';
import { CookieModule } from 'ngx-cookie';

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    LoginModule,
    RegisterModule,
    MainModule,
    StartupModule,
    HomeModule,
    CreateworkoutModule,
    WorkoutListModule,
    CookieModule.forRoot(),
  ],
  providers: [
    PostDataService
  ],
  bootstrap: [AppComponent],
})
export class AppModule { }
