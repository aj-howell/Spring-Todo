import { Component, OnInit } from '@angular/core';
import { SubTask } from '../SubTask';
import { Task } from '../Task';
import { TaskService } from '../task.service';


@Component({
  selector: 'app-task-dash-board',
  templateUrl: './task-dash-board.component.html',
  styleUrls: ['./task-dash-board.component.css']
})
export class TaskDashBoardComponent implements OnInit {

  userTasks: Array<Task> = [];
  userSubTasks: SubTask[]=[];
  displaySubtasks: boolean =false;
  displayPrayer:boolean=false;
  tableTitle:string ="My Tasks"
  prayerTask!:Task;

  constructor(private taskService:TaskService)
  {
  };
  
  ngOnInit(): void {
      this.getUserTasks(2);
      //this.getPrayerTask(2);
   }
 
  getUserTasks(userID:Number):void
  {
     this.taskService.getAllUserTasks(userID)
     .subscribe(
     {
        next: (res: Array<Task>)=>
        {
           this.userTasks=res;

           this.userSubTasks = [];

           this.userTasks.forEach((t)=>
           {
               if (t.subTasks) {
                  this.userSubTasks.push(...t.subTasks);
               } 
           })
        }

     })
  }

  getPrayerTask(userID:number):void
  {
      this.taskService.getPrayerTask(2)
      .subscribe({
         next:(res:Task)=>
         {
            this.prayerTask=res;
         }
      })
  }

  toggleSubtasks():void
  {
      if(this.displaySubtasks==false)
      {
         this.displaySubtasks=true;
         this.displayPrayer=false;
         this.tableTitle="My SubTasks";
      }
  }

  toggleTasks():void
  {
      this.displaySubtasks=false;
      this.displayPrayer=false;
      this.tableTitle="My Tasks";
      this.getUserTasks(2);
  }

  togglePrayerTask():void
  {
    
         this.displaySubtasks=false;
         this.displayPrayer=true;
         this.tableTitle="Prayers";
         this.getPrayerTask(2);
  }
}
