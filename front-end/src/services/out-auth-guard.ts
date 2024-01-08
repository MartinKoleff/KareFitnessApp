import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { CookieService } from 'ngx-cookie';

@Injectable({
  providedIn: 'root',
})
export class OutAuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private cookieService: CookieService
  ) {}
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const token = this.cookieService.get('token');

    if (!token) {
      // User has no valid token, prevent access to user pages
      this.router.navigate(['/register']); // Redirect to your register
      return false;
    }

    // User doesn't have a valid token, allow access to user pages
    return true;
  }
}
