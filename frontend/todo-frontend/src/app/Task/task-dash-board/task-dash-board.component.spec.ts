import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskDashBoardComponent } from './task-dash-board.component';

describe('TaskDashBoardComponent', () => {
  let component: TaskDashBoardComponent;
  let fixture: ComponentFixture<TaskDashBoardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TaskDashBoardComponent]
    });
    fixture = TestBed.createComponent(TaskDashBoardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
