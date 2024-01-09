import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap} from 'rxjs';
import {GetUser, PostUser} from '../app/models/user.model';
import { CookieService } from 'ngx-cookie';
import {Workout} from "../app/models/exercise.model";

@Injectable({
  providedIn: 'root',
})
export class PostDataService {
  private apiUrl = '../../assets/mock-posts.json'; // for fetching data
  private loginUrl = '/api/user/login'; //get
  private registerUrl= '/api/user/register'; //post
  private workoutSelectUrl= '/api/workout/select'; // post
  private workoutSelectedUrl= '/api/workout/selected'; // get
  private workoutGetUrl= '/api/workout/get'; // post
  private workoutAllUrl= '/api/workout/all'; // get
  private wokroutUpdateUrl= '/api/workout/update'; // put
  private workoutDelete = '/api/workout/delete'; //delete
  private workoutAdd_fullUrl= '/api/workout/add_full'; // post //adds full workout


  constructor(private http: HttpClient,  private cookieService: CookieService) {}

  getPosts(): Observable<any> {
    return this.http.get(this.apiUrl);
  }

  saveUser(user: PostUser): Observable<any> {
    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');

    // Check if there's already a token or user ID in cookies
    const existingToken = this.cookieService.get('token');
    const existingUserId = this.cookieService.get('userId');

    if (existingToken || existingUserId) {
      // Cookies already have values, handle it based on your requirements
      alert('User is already logged in.');
    }

    // Cookies are empty, proceed with the API call
    return this.http.post(this.registerUrl, JSON.stringify(user), { headers: headers }).pipe(
      tap((response: any) => {
        const userId = response.id;
        const token = response.token;

        // Store token and user ID in cookies
        this.cookieService.put('token', token);
        this.cookieService.put('userId', userId);

      })
    );
  }

  login(user: GetUser): Observable<any> {

    const headers = new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8');

    // Check if there's already a token or user ID in cookies
    const existingToken = this.cookieService.get('token');
    const existingUserId = this.cookieService.get('userId');

    if (existingToken || existingUserId) {
      // Cookies already have values, handle it based on your requirements
      alert('User is already logged in.');
    }

    // Cookies are empty, proceed with the API call
    return this.http.post(this.loginUrl, JSON.stringify(user), {headers: headers}).pipe(
      tap((response: any) => {
        const userId = response.id;
        const token = response.token;

        // Store token and user ID in cookies
        this.cookieService.put('token', token);
        this.cookieService.put('userId', userId);

      })
    );
  }
  submitWorkout(workout: Workout): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json; charset=utf-8',
      'Authorization': `Bearer ${this.cookieService.get('token')}`
    });
    return this.http.post(this.workoutAdd_fullUrl, workout, { headers });
  }
  getWorkouts(): Observable<any> {
    const token = this.cookieService.get('token');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json; charset=utf-8',
      'Authorization': `Bearer ${token}`

    });
    return this.http.get(this.workoutAllUrl, { headers });
  }
  deleteWorkout(workoutId: string): Observable<any> {
    const token = this.cookieService.get('token');
    const headers = new HttpHeaders({
      'Content-Type': 'application/json; charset=utf-8',
      'Authorization': `Bearer ${token}`
    });

    const url = this.workoutDelete;
    const options = { headers, body: { id : workoutId } };

    return this.http.delete(url, options);
  }
}
