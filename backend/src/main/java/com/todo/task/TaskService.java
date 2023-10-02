package com.todo.task;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.todo.Priority;
import com.todo.TodoDAO;
import com.todo.Exceptions.ResourceNotFoundException;
import com.todo.task.subtask.SubTask;
import com.todo.task.subtask.SubTaskRegistrationRequest;

@Service
public class TaskService {
     /*throw proper exceptions */
    private final TodoDAO taskDao;

    public TaskService(@Qualifier("jdbc")TodoDAO taskDao) //@qualifier is needed to autowire the dao to the JDBC access service
    {
        this.taskDao=taskDao;
    }

    public List<Task> getTasks()
    {
        return taskDao.GetAllTasks();
    }

    public void createTask(TaskRegistrationRequest task)
    {
        boolean isPriorityOption = Arrays.stream(Priority.values()).anyMatch(p->p.toString().equals(task.priority())); // if it matches high, medium, or low 

        if(isPriorityOption)
        {
            taskDao.CreateTask(task);
        }
        
    }
    public void editTask(Integer id,TaskRegistrationRequest task)
    {
        Task selected=taskDao.GetTaskById(id)
        .orElseThrow(()-> new ResourceNotFoundException("Task was not found"));
        
        boolean changes = false; // need the flag to know if changes were made 

            if(task.description()!= null && !selected.getDescription().equals(task.description()))
            {
                selected.setDescription(task.description());
                changes=true;
            }

             if(task.topic() != null && !selected.getTopic().equals(task.topic()))
            {
                selected.setTopic(task.topic());
                changes=true;
            }

             if(task.taskName() !=null && !selected.getTaskName().equals(task.taskName()))
            {
                selected.setTaskName(task.taskName());
                changes=true;
            }

             if(task.priority()!= null && !selected.getPriority().equals(task.priority()))
            {
                boolean isPriorityOption = Arrays.stream(Priority.values()).anyMatch(p->p.toString().equals(task.priority()));

                if(isPriorityOption)
                {
                    selected.setPriority(task.priority());
                    changes=true;
                }
                
            }
            
            if(!changes)
            {
                System.out.println("No changes made");
            }
        
        taskDao.EditTask(id, task);
    }

    public void deleteTask(Integer taskId) 
    {
       Task selected= taskDao.GetTaskById(taskId)
       .orElseThrow(()-> new ResourceNotFoundException("Task was not found"));

       // if there are subtask delete them first
        if(taskDao.GetSubTaskByTaskId(taskId).size()>0)
        {
            taskDao.DeleteSubTasksByTaskID(taskId); // deletes all subtask first
            
            //clear out the subtask in the task;
            selected.getSubTasks().clear();
        }

       taskDao.DeleteTaskByID(taskId);
    };
    
    public Task getTask(Integer taskId)
    {
       return taskDao.GetTaskById(taskId)
       .orElseThrow(()-> new ResourceNotFoundException("Task was not found"));
    }


    public void CreateSubTask(Integer taskId, SubTaskRegistrationRequest task)
    {
        Task selected= taskDao.GetTaskById(taskId)
       .orElseThrow(()-> new ResourceNotFoundException("Task was not found"));

        taskDao.CreateSubTask(task);

        //select the most recent subtask inserted under that user
        SubTask inserted = taskDao.GetSubTaskByTaskId(taskId).get(0);


        //add into sublists array inside selected
        selected.addSubtask(inserted);

        System.out.println("length of sublists: "+selected.getSubTasks().size());
        
    }
};