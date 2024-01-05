import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PostUser } from '../app/models/user.model';

@Injectable({
  providedIn: 'root'
})
export class PostDataService {
  private apiUrl = '../../assets/mock-posts.json'; // for fetching data
  private loginUrl = '/api/user/login';
  private registerUrl= '/api/user/register';
  private exerciseAddUrl= '/api/exercise/add';
  private workoutSelectUrl= '/api/workout/select';
  private workoutSelectedUrl= '/api/workout/selected';
  private exerciseGetUrl= '/api/exercise/get';
  private exerciseAllUrl= '/api/exercise/all';
  private workoutAddUrl= '/api/workout/add';
  private workoutGetUrl= '/api/workout/get';
  private workoutAllUrl= '/api/workout/all';
  private exerciseUpdateUrl= '/api/exercise/update';
  private wokroutUpdateUrl= '/api/workout/update';
  private programAllUrl = '/api/program/all';
  private programAddUrl = '/api/program/add';
  private programGetUrl = '/api/program/get';
  private programUpdateUrl= '/api/program/update';
  private workoutAdd_fullUrl= '/api/workout/add_full';

  constructor(private http: HttpClient) {}

  getPosts(): Observable<any> {
    return this.http.get(this.apiUrl);
  }

  saveUser(user: PostUser): Observable<any> {
    return this.http.post(this.registerUrl, JSON.stringify(user));
  }




  getAuthenticatedData(token: string): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get(this.apiUrl, { headers });
  }
}
