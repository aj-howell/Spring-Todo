package com.todo;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.javafaker.Faker;
import com.todo.Location.MyLocationRegistrationRequest;
import com.todo.Location.MyLocationRowMapper;
import com.todo.User.UserRegistrationRequest;
import com.todo.User.UserRowMapper;
import com.todo.task.Task;
import com.todo.task.TaskRegistrationRequest;
import com.todo.task.TaskRowMapper;
import com.todo.task.subtask.SubTaskRowMapper;

public class JDBCTasksTest extends AbstractTestcontainersUnitTests 
{
    private JDBCAccessTemplate underTest;
    private TaskRowMapper taskRowMapper;
    private UserRowMapper userRowMapper;
    private SubTaskRowMapper subTaskRowMapper;
    private MyLocationRowMapper locationMapper;
    private final Faker faker = new Faker();

    @BeforeEach
    void setup()
    {
        taskRowMapper = new TaskRowMapper();
        userRowMapper = new UserRowMapper();
        subTaskRowMapper = new SubTaskRowMapper();
        locationMapper = new MyLocationRowMapper();

        underTest = new JDBCAccessTemplate(getJDBC(), taskRowMapper, subTaskRowMapper, userRowMapper, locationMapper);
    }

    private String getRandomUserName() {
        return faker.name().username();
    }
    
    private String getRandomEmail() {
        return faker.internet().emailAddress();
    }

    private String getRandomTaskName() {
        return faker.lorem().word();
    }
    
    private String getRandomPhoneNumber() {
        return faker.phoneNumber().phoneNumber();
    }

    @Test
    public void GetAllTasks() {
        String randomUserName = getRandomUserName();
        String randomEmail = getRandomEmail();
        String randomTaskName = getRandomTaskName();
        String randomPhoneNumber = getRandomPhoneNumber();
    
        MyLocationRegistrationRequest locationRequest = new MyLocationRegistrationRequest(
                123.456f,
                78.910f,
                "Eastern Time",
                "Boston"
        );
        underTest.CreateLocation(locationRequest);
    
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest(
                randomUserName,
                randomEmail,
                "password123",
                randomPhoneNumber,
                1
        );
    
        underTest.CreateUser(registrationRequest);
    
        TaskRegistrationRequest task = new TaskRegistrationRequest(LocalDate.now(), LocalDate.now(), randomTaskName, "Dishes", "There are a sink of dishes to do", "High", 1);
    
        underTest.CreateTask(task);
    
        assertThat(underTest.GetAllTasks()).isNotNull();
    }
    
    @Test
    public void GetTaskById() {
        String randomUserName = getRandomUserName();
        String randomEmail = getRandomEmail();
        String randomTaskName = getRandomTaskName();
        String randomPhoneNumber = getRandomPhoneNumber();
    
        MyLocationRegistrationRequest locationRequest = new MyLocationRegistrationRequest(
                123.456f,
                78.910f,
                "Eastern Time",
                "Boston"
        );
        underTest.CreateLocation(locationRequest);
    
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest(
                randomUserName,
                randomEmail,
                "password123",
                randomPhoneNumber,
                1
        );
    
        underTest.CreateUser(registrationRequest);
    
        TaskRegistrationRequest task = new TaskRegistrationRequest(LocalDate.now(), LocalDate.now(), randomTaskName, "Dishes", "There are a sink of dishes to do", "High", 1);
    
        underTest.CreateTask(task);

    
        Integer taskId = underTest.GetAllTasks()
                .stream()
                .filter(t->t.getTaskName().equals(randomTaskName+"-"+task.user_id()))
                .map(t2-> t2.getTaskId())
                .findFirst()
                .get();
    
        Optional<Task> selected = underTest.GetTaskById(taskId)
                .stream()
                .findFirst();
    
        assertThat(selected).isPresent().hasValueSatisfying(t -> {
            assertThat(t.getDescription()).isEqualTo(task.description());
            assertThat(t.getTaskId()).isEqualTo(taskId);
            assertThat(t.getDueDate()).isEqualTo(task.dueDate());
            assertThat(t.getStartDate()).isEqualTo(task.startDate());
            assertThat(t.getPriority()).isEqualTo(task.priority());
            assertThat(t.getTaskName()).isEqualTo(task.taskName()+"-"+task.user_id());
            assertThat(t.getTopic()).isEqualTo(task.topic());
            assertThat(t.getUser_id()).isEqualTo(task.user_id());
        });
    }
    
    @Test
    public void EditTask() {
        String randomUserName = getRandomUserName();
        String randomEmail = getRandomEmail();
        String randomTaskName = getRandomTaskName();
        String randomPhoneNumber = getRandomPhoneNumber();
    
        MyLocationRegistrationRequest locationRequest = new MyLocationRegistrationRequest(
                123.456f,
                78.910f,
                "Eastern Time",
                "Boston"
        );
        underTest.CreateLocation(locationRequest);
    
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest(
                randomUserName,
                randomEmail,
                "password123",
                randomPhoneNumber,
                1
        );
    
        underTest.CreateUser(registrationRequest);
    
        TaskRegistrationRequest task = new TaskRegistrationRequest(LocalDate.now(), LocalDate.now(), randomTaskName, "Dishes", "There are a sink of dishes to do", "High", 1);
    
        underTest.CreateTask(task);

        Integer taskId = underTest.GetAllTasks()
                .stream()
                .filter(t -> t.getTaskName().equals(task.taskName() + "-" + task.user_id()))
                .map(s -> s.getTaskId())
                .findFirst()
                .orElseThrow();

        TaskRegistrationRequest updatedTask = new TaskRegistrationRequest(null, null, randomTaskName, "Trash", "Take out the trash", "High", 1);
        
        underTest.EditTask(taskId, updatedTask);
            
        Optional<Task> selected = underTest.GetTaskById(taskId)
                .stream()
                .findFirst();
    
        assertThat(selected).isPresent().hasValueSatisfying(t -> {
            assertThat(t.getDescription()).isEqualTo(updatedTask.description());
            assertThat(t.getTaskId()).isEqualTo(taskId);
            assertThat(t.getDueDate()).isNotEqualTo(updatedTask.dueDate());
            assertThat(t.getStartDate()).isNotEqualTo(updatedTask.startDate());
            assertThat(t.getPriority()).isEqualTo(updatedTask.priority());
            assertThat(t.getTaskName()).isEqualTo(updatedTask.taskName()+"-"+updatedTask.user_id());
            assertThat(t.getTopic()).isEqualTo(updatedTask.topic());
            assertThat(t.getUser_id()).isEqualTo(updatedTask.user_id());
        });
    }

    @Test
    public void CannotEditTask() {
        String randomUserName = getRandomUserName();
        String randomEmail = getRandomEmail();
        String randomTaskName = getRandomTaskName();
        String randomPhoneNumber = getRandomPhoneNumber();
    
        MyLocationRegistrationRequest locationRequest = new MyLocationRegistrationRequest(
                123.456f,
                78.910f,
                "Eastern Time",
                "Boston"
        );
        underTest.CreateLocation(locationRequest);
    
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest(
                randomUserName,
                randomEmail,
                "password123",
                randomPhoneNumber,
                1
        );
    
        underTest.CreateUser(registrationRequest);
    
        TaskRegistrationRequest task = new TaskRegistrationRequest(LocalDate.now(), LocalDate.now(), randomTaskName+"55", "Dishes", "There are a sink of dishes to do", "High", 1);
    
        underTest.CreateTask(task);

        Integer taskId = underTest.GetAllTasks()
                .stream()
                .filter(t -> t.getTaskName().equals(task.taskName() + "-" + task.user_id()))
                .map(s -> s.getTaskId())
                .findFirst()
                .orElseThrow();

        TaskRegistrationRequest updatedTask = new TaskRegistrationRequest(null, null, null, null, null, null, null);
        
        try {
             underTest.EditTask(taskId, updatedTask);   
        } catch (Exception e) {
              Optional<Task> selected = underTest.GetTaskById(taskId)
                .stream()
                .findFirst();
    
        assertThat(selected).isPresent().hasValueSatisfying(t -> {
            assertThat(t.getDescription()).isNotEqualTo(updatedTask.description());
            assertThat(t.getDueDate()).isNotEqualTo(updatedTask.dueDate());
            assertThat(t.getStartDate()).isNotEqualTo(updatedTask.startDate());
            assertThat(t.getPriority()).isNotEqualTo(updatedTask.priority());
            assertThat(t.getTaskName()).isNotEqualTo(updatedTask.taskName()+"-"+updatedTask.user_id());
            assertThat(t.getTopic()).isNotEqualTo(updatedTask.topic());
            assertThat(t.getUser_id()).isNotEqualTo(updatedTask.user_id());
        });
        }
            
    }
    
    @Test
    public void DeleteTaskById() {
        String randomUserName = getRandomUserName();
        String randomEmail = getRandomEmail();
        String randomTaskName = getRandomTaskName();
        String randomPhoneNumber = getRandomPhoneNumber();
    
        MyLocationRegistrationRequest locationRequest = new MyLocationRegistrationRequest(
                123.456f,
                78.910f,
                "Eastern Time",
                "Boston"
        );
        underTest.CreateLocation(locationRequest);
    
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest(
                randomUserName,
                randomEmail,
                "password123",
                randomPhoneNumber,
                1
        );
    
        underTest.CreateUser(registrationRequest);
    
        TaskRegistrationRequest task = new TaskRegistrationRequest(LocalDate.now(), LocalDate.now(), randomTaskName, "Dishes", "There are a sink of dishes to do", "High", 1);
    
        underTest.CreateTask(task);
    
        Integer taskId = underTest.GetAllTasks()
                .stream()
                .filter(t -> t.getTaskName().equals(task.taskName() + "-" + task.user_id()))
                .map(s -> s.getTaskId())
                .findFirst()
                .orElseThrow();
    
        underTest.DeleteTaskByID(taskId);
    
        boolean isMatch = underTest.GetAllTasks()
                .stream()
                .anyMatch(t -> t.getTaskId().equals(taskId));
    
        assertThat(isMatch).isFalse();
    }
    
    @Test
    public void DeleteTaskById_UnsuccessfulDelete() {
        String randomUserName = getRandomUserName();
        String randomEmail = getRandomEmail();
        String randomPhoneNumber = getRandomPhoneNumber();
    
        MyLocationRegistrationRequest locationRequest = new MyLocationRegistrationRequest(
                123.456f,
                78.910f,
                "Eastern Time",
                "Boston"
        );
        underTest.CreateLocation(locationRequest);
    
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest(
                randomUserName,
                randomEmail,
                "password123",
                randomPhoneNumber,
                1
        );
    
        underTest.CreateUser(registrationRequest);
    
        underTest.DeleteTaskByID(0);
    
        boolean ans = underTest.CheckIfTaskExists(0);
    
        assertThat(ans).isFalse();
    }
    
    @Test
    public void CheckIfTaskExists() {
        String randomUserName = getRandomUserName();
        String randomEmail = getRandomEmail();
        String randomTaskName = getRandomTaskName();
        String randomPhoneNumber = getRandomPhoneNumber();
    
        MyLocationRegistrationRequest locationRequest = new MyLocationRegistrationRequest(
                123.456f,
                78.910f,
                "Eastern Time",
                "Boston"
        );
        underTest.CreateLocation(locationRequest);
    
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest(
                randomUserName,
                randomEmail,
                "password123",
                randomPhoneNumber,
                1
        );
    
        underTest.CreateUser(registrationRequest);
    
        TaskRegistrationRequest task = new TaskRegistrationRequest(LocalDate.now(), LocalDate.now(), randomTaskName, "Dishes", "There are a sink of dishes to do", "High", 1);
    
        underTest.CreateTask(task);
    
        Integer taskId = underTest.GetAllTasks()
                .stream()
                .filter(t -> t.getTaskName().equals(task.taskName() + "-" + task.user_id()))
                .map(s -> s.getTaskId())
                .findFirst()
                .orElseThrow();
    
        boolean isPresent = underTest.CheckIfTaskExists(taskId);
    
        assertThat(isPresent).isTrue();
    }
    
    @Test
    public void CheckIfTaskExistsFalse() {
        String randomUserName = getRandomUserName();
        String randomEmail = getRandomEmail();
        String randomTaskName = getRandomTaskName();
        String randomPhoneNumber = getRandomPhoneNumber();
    
        MyLocationRegistrationRequest locationRequest = new MyLocationRegistrationRequest(
                123.456f,
                78.910f,
                "Eastern Time",
                "Boston"
        );
        underTest.CreateLocation(locationRequest);
    
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest(
                randomUserName,
                randomEmail,
                "password123",
                randomPhoneNumber,
                1
        );
    
        underTest.CreateUser(registrationRequest);
    
        TaskRegistrationRequest task = new TaskRegistrationRequest(LocalDate.now(), LocalDate.now(), randomTaskName, "Dishes", "There are a sink of dishes to do", "High", 1);
    
        underTest.CreateTask(task);
    
        Integer taskId = underTest.GetAllTasks()
                .stream()
                .filter(t -> t.getTaskName().equals(task.taskName() + "-" + task.user_id()))
                .map(s -> s.getTaskId())
                .findFirst()
                .orElseThrow();
    
        underTest.DeleteTaskByID(taskId);
    
        boolean isPresent = underTest.CheckIfTaskExists(taskId);
    
        assertThat(isPresent).isFalse();
    }
}
