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
  private exerciseAddUrl= 'http://64.225.102.27:8085/api/exercise/add';
  private workoutSelectUrl= 'http://64.225.102.27:8085/api/workout/select';
  private workoutSelectedUrl= 'http://64.225.102.27:8085/api/workout/selected';
  private exerciseGetUrl= 'http://64.225.102.27:8085/api/exercise/get';
  private exerciseAllUrl= 'http://64.225.102.27:8085/api/exercise/all';
  private workoutAddUrl= 'http://64.225.102.27:8085/api/workout/add';
  private workoutGetUrl= 'http://64.225.102.27:8085/api/workout/get';
  private workoutAllUrl= 'http://64.225.102.27:8085/api/workout/all';
  private exerciseUpdateUrl= 'http://64.225.102.27:8085/api/exercise/update';
  private wokroutUpdateUrl= 'http://64.225.102.27:8085/api/workout/update';
  private programAllUrl = 'http://64.225.102.27:8085/api/program/all';
  private programAddUrl = 'http://64.225.102.27:8085/api/program/add';
  private programGetUrl = 'http://64.225.102.27:8085/api/program/get';
  private programUpdateUrl= 'http://64.225.102.27:8085/api/program/update';
  private workoutAdd_fullUrl= 'http://64.225.102.27:8085/api/workout/add_full';

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
