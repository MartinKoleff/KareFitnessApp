import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardioComponent } from './cardio.component';

describe('CardioComponent', () => {
  let component: CardioComponent;
  let fixture: ComponentFixture<CardioComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CardioComponent]
    });
    fixture = TestBed.createComponent(CardioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
