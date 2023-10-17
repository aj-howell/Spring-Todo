import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TaskDashBoardComponent } from './Task/task-dash-board/task-dash-board.component';
import { TaskRowComponent } from './Task/task-row/task-row.component';

@NgModule({
  declarations: [
    AppComponent,
    TaskDashBoardComponent,
    TaskRowComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
