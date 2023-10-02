package com.todo.task;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo.task.subtask.SubTaskRegistrationRequest;


@RestController
@RequestMapping("/api/v1/logbook")
public class TaskController
{
    private TaskService taskService;

    public TaskController(TaskService taskService)
    {
        this.taskService=taskService;
    }

    @GetMapping()
    public List<Task> getTasks() {
      return taskService.getTasks();
    }

    @PostMapping()
    public void createTask(@RequestBody TaskRegistrationRequest task)
    {
        taskService.createTask(task);
    }

    @PostMapping("{taskId}/subtasks")
    public void createTask(@PathVariable("taskId") Integer id,@RequestBody SubTaskRegistrationRequest task)
    {
        taskService.CreateSubTask(id, task);
    }

    @PutMapping("{taskId}")
    public void editTask(@PathVariable("taskId") Integer id, @RequestBody TaskRegistrationRequest task)
    {
        taskService.editTask(id, task);
    }

    @DeleteMapping("{taskId}")
    public void deleteTask(@PathVariable("taskId") Integer taskId) {
        taskService.deleteTask(taskId);
    }

        @GetMapping("{taskId}")
    public Task getTask(@PathVariable("taskId") Integer taskId) {
       return taskService.getTask(taskId);
    }
        
}