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
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from '../services/auth-interceptor.service';
import { CreateworkoutModule } from './createworkout/createworkout.module';
import { BicepsComponent } from './exercise-components/biceps/biceps.component';
import { BackComponent } from './exercise-components/back/back.component';
import { TricepsComponent } from './exercise-components/triceps/triceps.component';
import { LegsComponent } from './exercise-components/legs/legs.component';
import { ShouldersComponent } from './exercise-components/shoulders/shoulders.component';
import { ChestComponent } from './exercise-components/chest/chest.component';
import { CardioComponent } from './exercise-components/cardio/cardio.component';
import { AbsComponent } from './exercise-components/abs/abs.component';

@NgModule({
  declarations: [
    AppComponent,
    BicepsComponent,
    BackComponent,
    TricepsComponent,
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
    LoginModule,
    RegisterModule,
    MainModule,
    StartupModule,
    HomeModule,
    CreateworkoutModule

  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    PostDataService
  ],
  bootstrap: [AppComponent],
})
export class AppModule { }
