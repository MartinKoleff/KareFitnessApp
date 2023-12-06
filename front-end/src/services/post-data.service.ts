import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PostUser } from '../app/models/user.model';

@Injectable({
  providedIn: 'root'
})
export class PostDataService {
  private apiUrl='../../assets/mock-posts.json';
  constructor(private http: HttpClient) {
  }
  getPosts(){
    let url = 'Place json url here'
    return this.http.get(this.apiUrl)
  }
  saveUser(user: PostUser): Observable<any> {
    return this.http.post(this.apiUrl, JSON.stringify(user));
  }
  getAuthenticatedData(token: string): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get(this.apiUrl, { headers });
  }
}
