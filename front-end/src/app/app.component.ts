import { Component, ViewEncapsulation } from '@angular/core';
import {NavigationEnd, Router} from "@angular/router";
import {filter} from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AppComponent {
  title = 'front-end';
  constructor(private router: Router) {
    this.router.events.pipe(
      filter(event=> event instanceof NavigationEnd)
    ).subscribe(()=>{
      this.updateNavbarVisibility();
    });
  }
  isStartupRoute(): boolean{
    const restrictedRoutes = ['/startup' , '/register', '/login']
    return restrictedRoutes.includes(this.router.url)
  }
  private updateNavbarVisibility(): void{

  }
}
