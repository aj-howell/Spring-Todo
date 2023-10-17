import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Task } from './Task';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  url:String = `http://localhost:8088/api/v1/logbook/`
 

  constructor(private httpClient:HttpClient){}


  // createTask(request:TaskRegistrationRequest):void
  // {
  // }

  // deleteTask(taskID: Number):void
  // {

  // }

  // editTask(taskID:Number,request:TaskRegistrationRequest):void
  // {

  // }

  getTask(taskID:Number):Observable<Task>
  {
    return this.httpClient.get<Task>(this.url+taskID.toString());
  }

  getPrayerTask(userID:number):Observable<Task>
  {
    return this.httpClient.get<Task>("http://localhost:8088/api/v1/"+`users/${userID}/prayer`);
  }

  getAllUserTasks(userID:Number): Observable<Array<Task>>
  {
   return this.httpClient.get<Array<Task>>(this.url+`user-tasks/${userID}`);
  }
}
