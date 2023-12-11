import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShouldersComponent } from './shoulders.component';

describe('ShouldersComponent', () => {
  let component: ShouldersComponent;
  let fixture: ComponentFixture<ShouldersComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ShouldersComponent]
    });
    fixture = TestBed.createComponent(ShouldersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
