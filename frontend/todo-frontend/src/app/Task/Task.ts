import { SubTask } from "./SubTask";

export interface Task
{
    taskId:number;
    taskName: string;
    description: string;
    topic:string
    startDate: Date;
    dueDate: Date;
    priority:string;
    subTasks: Array<SubTask>;
    userId:number;
}