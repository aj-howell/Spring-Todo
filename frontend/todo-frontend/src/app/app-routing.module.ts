import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TaskDashBoardComponent } from './Task/task-dash-board/task-dash-board.component';

const routes: Routes = [
   {
    path:"dashboard",
    component: TaskDashBoardComponent
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
