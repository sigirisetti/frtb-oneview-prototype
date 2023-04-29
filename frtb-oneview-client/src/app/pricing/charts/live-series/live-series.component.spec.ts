import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LiveSeriesComponent } from './live-series.component';

describe('LiveSeriesComponent', () => {
  let component: LiveSeriesComponent;
  let fixture: ComponentFixture<LiveSeriesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LiveSeriesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LiveSeriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
