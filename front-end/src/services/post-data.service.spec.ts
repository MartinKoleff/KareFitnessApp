import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LoginComponent } from '../app/login/login.component';
import { PostDataService } from './post-data.service';
import { Component } from '@angular/core';

describe('PostDataService', () => {
  let service: PostDataService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientTestingModule],
      providers:[PostDataService],
    });
    service = TestBed.inject(PostDataService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(()=>{
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve posts from the API via GET', ()=>{
    const testData=[
      {email: 'admin', password: 'admin', username: 'admin'},
      {email: 'admin2',password: 'admin2',username: 'admin2'},
    ];
    service.getPosts().subscribe(posts=>{
      expect(posts).toEqual(testData);
    })
    const req = httpTestingController.expectOne('../../assets/mock-posts.json');
    expect(req.request.method).toEqual('GET');
    req.flush(testData);
  })
});


