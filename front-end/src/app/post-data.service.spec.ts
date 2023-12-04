import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { PostDataService } from './post-data.service';

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
      {id:1, title: 'Post 1', content: 'This is the content of Post 1'},
      {id:2, title: 'Post 2', content: 'This is the content of Post 2'},
    ];
    service.getPosts().subscribe(posts=>{
      expect(posts).toEqual(testData);
    })
    const req = httpTestingController.expectOne('../../assets/mock-posts.json');
    expect(req.request.method).toEqual('GET');
    req.flush(testData);
  })
});
