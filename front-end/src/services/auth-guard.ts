import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from './auth-service';
import { CookieService } from 'ngx-cookie';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router,
    private cookieService: CookieService
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const token = this.cookieService.get('token');
    const userId = this.cookieService.get('userId')

    if (token && userId) {
      // User has a valid token, prevent access to /login or /register
      this.router.navigate(['/main']); // Redirect to main route
      return false;
    }
    else{
      //User don't have valid token, prevent them from entering the user pages
      this.router.navigate(['/register']); //Redirect to register page
    }

    // User doesn't have a valid token, allow access to /login or /register
    return true;
  }
}
