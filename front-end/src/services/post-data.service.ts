import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PostUser } from '../app/models/user.model';

@Injectable({
  providedIn: 'root'
})
export class PostDataService {
  private apiUrl = '../../assets/mock-posts.json'; // for fetching data
  private saveUrl = '../../assets/mock-posts.json'; // replace with your actual save endpoint
  private loginUrl = 'http://64.225.102.27:8085/api/user/login';
  private registerUrl= 'http://64.225.102.27:8085/api/user/register';


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
