package com.todo;

import java.util.List;
import java.util.Optional;

import com.todo.Location.MyLocation;
import com.todo.Location.MyLocationRegistrationRequest;
import com.todo.User.User;
import com.todo.User.UserRegistrationRequest;
import com.todo.task.Task;
import com.todo.task.TaskRegistrationRequest;
import com.todo.task.subtask.SubTask;
import com.todo.task.subtask.SubTaskRegistrationRequest;

public interface TodoDAO
{
    //Tasks
    public List<Task> GetAllTasks();
    public void CreateTask(TaskRegistrationRequest task);
    public void EditTask(Integer id,TaskRegistrationRequest task);
    public void DeleteTaskByID(Integer taskId);
    public Optional<Task> GetTaskById(Integer taskId);
    public boolean CheckIfTaskExists(Integer taskId);
    public List<Task> GetTasksByUserID(Integer userId);
    public Optional<Task> GetTaskByName(String task_name);
    //Subtasks
    public Optional<SubTask> GetSubTask(Integer taskId);
    public List<SubTask> GetSubTaskByTaskId(Integer subTaskId);
    public void DeleteSubTaskByID(Integer taskId);
    public void DeleteSubTasksByTaskID(Integer taskId);
    public void CreateSubTask(SubTaskRegistrationRequest task);
    public List<SubTask> GetAllSubTask();
    //Users
    public void CreateUser(UserRegistrationRequest user);
    public List<User> GetAllUsers();
    public void EditUser(Integer userId, UserRegistrationRequest user);
    public void DeleteUserById(Integer userId);
    public Optional<User> GetUserById(Integer userId);
    public void EditUserLocation(Integer userId, Integer location_id);
    //Locations
    public List<MyLocation> GetAllLocations();
    public Optional<MyLocation> GetLocationById(Integer locationId);
    public void CreateLocation(MyLocationRegistrationRequest location);
    public void UpdateLocation(Integer locationId, MyLocationRegistrationRequest location);
}