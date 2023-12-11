import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BicepsComponent } from './biceps.component';

describe('BicepsComponent', () => {
  let component: BicepsComponent;
  let fixture: ComponentFixture<BicepsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BicepsComponent]
    });
    fixture = TestBed.createComponent(BicepsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
