import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

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
}
