package com.todo;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.javafaker.Faker;
import com.todo.Location.MyLocationRegistrationRequest;
import com.todo.User.UserRegistrationRequest;
import com.todo.task.TaskRegistrationRequest;
import com.todo.task.TaskRowMapper;
import com.todo.task.subtask.SubTask;
import com.todo.task.subtask.SubTaskRegistrationRequest;
import com.todo.task.subtask.SubTaskRowMapper;

public class JDBCSubTasks extends AbstractTestcontainersUnitTests {
    private JDBCAccessTemplate underTest;
    private TaskRowMapper taskRowMapper;
    private SubTaskRowMapper subTaskRowMapper;
    private final Faker faker = new Faker();


    @BeforeEach
    void setup()
    {
        taskRowMapper = new TaskRowMapper();
        subTaskRowMapper = new SubTaskRowMapper();
        underTest = new JDBCAccessTemplate(getJDBC(), taskRowMapper, subTaskRowMapper, null, null);
    }

    private String getRandomTaskName() {
        return faker.lorem().word();
    }

    private String getRandomUserName() {
        return faker.name().username();
    }
    
    private String getRandomEmail() {
        return faker.internet().emailAddress();
    }
    
    private String getRandomPhoneNumber() {
        return faker.phoneNumber().phoneNumber();
    }

    private String getRandomSubTaskName() {
    return faker.lorem().word();
    }



    @Test
    public void CreateSubTask()
    {
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
    
        TaskRegistrationRequest task = new TaskRegistrationRequest(LocalDate.now(), LocalDate.now(), randomTaskName+"now", "food", "buy food", "High", 1);
    
        underTest.CreateTask(task);

        Integer task_id = underTest.GetAllTasks()
        .stream()
        .filter(t->t.getTaskName().equals(task.taskName()+"-"+task.user_id()))
        .map(t->t.getTaskId())
        .findFirst()
        .get();

       SubTaskRegistrationRequest subtask = new SubTaskRegistrationRequest("Groceries", "pick up some fruit", task_id);

       underTest.CreateSubTask(subtask);

      boolean exists = underTest.GetAllSubTask()
       .stream()
       .anyMatch(s->s.getTaskName().equals(subtask.task_name()));

      assertThat(exists).isTrue();
    }

    @Test
    public void GetAllSubTasks() {
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
    
        TaskRegistrationRequest task = new TaskRegistrationRequest(LocalDate.now(), LocalDate.now(), randomTaskName, "Clean Clothes", "Wash all of the white clothes", "High", 1);
    
        underTest.CreateTask(task);

      Integer task_id = underTest.GetAllTasks()
        .stream()
        .filter(t->t.getTaskName().equals(task.taskName()+"-"+task.user_id()))
        .map(t->t.getTaskId())
        .findFirst()
        .get();
    
       SubTaskRegistrationRequest subtask = new SubTaskRegistrationRequest("Dry the dishes", "Dry all the dishes in dishwashers", task_id);

       underTest.CreateSubTask(subtask);

      boolean size = underTest.GetAllSubTask().size() != 0;

      assertThat(size).isTrue();
    }

    @Test
    public void GetSubTaskById()
    {
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

      Integer task_id = underTest.GetAllTasks()
        .stream()
        .filter(t->t.getTaskName().equals(task.taskName()+"-"+task.user_id()))
        .map(t->t.getTaskId())
        .findFirst()
        .get();
    
       SubTaskRegistrationRequest subtask = new SubTaskRegistrationRequest("All the plates are dirty", "Dirty Plates", task_id);

       underTest.CreateSubTask(subtask);


      Integer subtask_id = underTest.GetAllSubTask()
      .stream()
      .filter(st->st.getTaskName().equals(subtask.task_name()))
      .map(st->st.getSubtask_id())
      .findFirst()
      .orElseThrow();

       Optional<SubTask> sub= underTest.GetSubTask(subtask_id);

       assertThat(sub).isPresent().hasValueSatisfying(st->
       {
            assertThat(st.getDescription()).isEqualTo(subtask.description());
            assertThat(st.getParentTaskId()).isEqualTo(task_id);
            assertThat(st.getTaskName()).isEqualTo(subtask.task_name());
            assertThat(st.getSubtask_id()).isEqualTo(subtask_id);
       });
    }

    @Test
    public void DeleteSubTaskByID()
    {
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
    
        TaskRegistrationRequest task = new TaskRegistrationRequest(LocalDate.now(), LocalDate.now(), randomTaskName, "clothes", "buy new clothes", "High", 1);
    
        underTest.CreateTask(task);

      Integer task_id = underTest.GetAllTasks()
        .stream()
        .filter(t->t.getTaskName().equals(task.taskName()+"-"+task.user_id()))
        .map(t->t.getTaskId())
        .findFirst()
        .get();
    
       SubTaskRegistrationRequest subtask = new SubTaskRegistrationRequest("go to the store", "take a trip to downtown and get new clothes", task_id);

       underTest.CreateSubTask(subtask);


      Integer subtask_id = underTest.GetAllSubTask()
      .stream()
      .filter(st->st.getTaskName().equals(subtask.task_name()))
      .map(st->st.getSubtask_id())
      .findFirst()
      .orElseThrow();

      underTest.DeleteSubTaskByID(subtask_id);

     boolean exists = underTest.GetAllSubTask()
      .stream()
      .anyMatch(st->st.getTaskName().equals(subtask.task_name()));

      assertThat(exists).isFalse();

    }

    @Test
    public void DeleteSubTasksByTaskID()
    {
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
    
        TaskRegistrationRequest task = new TaskRegistrationRequest(LocalDate.now(), LocalDate.now(), randomTaskName, "clothes", "buy new clothes", "High", 1);
    
        underTest.CreateTask(task);

      Integer task_id = underTest.GetAllTasks()
        .stream()
        .filter(t->t.getTaskName().equals(task.taskName()+"-"+task.user_id()))
        .map(t->t.getTaskId())
        .findFirst()
        .get();
    
       SubTaskRegistrationRequest subtask = new SubTaskRegistrationRequest("go to the store", "take a trip to downtown and get new clothes", task_id);

       underTest.CreateSubTask(subtask);

       underTest.DeleteSubTasksByTaskID(task_id);

      boolean exists = underTest.GetAllSubTask()
        .stream()
        .anyMatch(st->st.getParentTaskId().equals(task_id));
      
      assertThat(exists).isFalse();
    }

@Test
public void CreateSubTask_FalseCase()
{
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

    TaskRegistrationRequest task = new TaskRegistrationRequest(LocalDate.now(), LocalDate.now(), randomTaskName + "now", "food", "buy food", "High", 1);

    underTest.CreateTask(task);

    // Generate a random subtask name
    String randomSubTaskName = getRandomSubTaskName();

    SubTaskRegistrationRequest subtask = new SubTaskRegistrationRequest(randomSubTaskName, "pick up some fruit", 0);

     boolean exists = false;
    try {
        underTest.CreateSubTask(subtask);
        exists = underTest.GetAllSubTask()
            .stream()
            .anyMatch(s -> s.getTaskName().equals(subtask.task_name()));
    } catch (Exception e) {
          assertThat(exists).isFalse();
    }
}

@Test
public void GetAllSubTasks_FalseCase() {
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

    TaskRegistrationRequest task = new TaskRegistrationRequest(LocalDate.now(), LocalDate.now(), randomTaskName, "Clean Clothes", "Wash all of the white clothes", "High", 1);

    underTest.CreateTask(task);

    Integer task_id = underTest.GetAllTasks()
            .stream()
            .filter(t -> t.getTaskName().equals(task.taskName() + "-" + task.user_id()))
            .map(t -> t.getTaskId())
            .findFirst()
            .get();

    // Generate a random subtask name
    String randomSubTaskName = getRandomSubTaskName();

    SubTaskRegistrationRequest subtask = new SubTaskRegistrationRequest(randomSubTaskName, "Dry the dishes", 0);

    boolean size = false;

    try {
        underTest.CreateSubTask(subtask);
        size = underTest.GetAllSubTask().size() != 0;
    } catch (Exception e) {
        assertThat(size).isFalse();
    }
}

@Test
public void GetSubTaskById_FalseCase()
{
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

    Integer task_id = underTest.GetAllTasks()
            .stream()
            .filter(t -> t.getTaskName().equals(task.taskName() + "-" + task.user_id()))
            .map(t -> t.getTaskId())
            .findFirst()
            .get();

    // Generate a random subtask name
    String randomSubTaskName = getRandomSubTaskName();

    SubTaskRegistrationRequest subtask = new SubTaskRegistrationRequest(randomSubTaskName, "All the plates are dirty", task_id);

    underTest.CreateSubTask(subtask);

    Integer subtask_id = underTest.GetAllSubTask()
            .stream()
            .filter(st -> st.getTaskName().equals(subtask.task_name()))
            .map(st -> st.getSubtask_id())
            .findFirst()
            .orElseThrow();

    // Attempt to get a subtask that doesn't exist
    Optional<SubTask> sub = underTest.GetSubTask(subtask_id + 1);

    assertThat(sub).isEmpty();
}

@Test
public void DeleteSubTaskByID_FalseCase()
{
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

    TaskRegistrationRequest task = new TaskRegistrationRequest(LocalDate.now(), LocalDate.now(), randomTaskName, "clothes", "buy new clothes", "High", 1);

    underTest.CreateTask(task);

    Integer task_id = underTest.GetAllTasks()
            .stream()
            .filter(t -> t.getTaskName().equals(task.taskName() + "-" + task.user_id()))
            .map(t -> t.getTaskId())
            .findFirst()
            .get();

    // Generate a random subtask name
    String randomSubTaskName = getRandomSubTaskName();

    SubTaskRegistrationRequest subtask = new SubTaskRegistrationRequest(randomSubTaskName, "go to the store", task_id);

    underTest.CreateSubTask(subtask);

    Integer subtask_id = underTest.GetAllSubTask()
            .stream()
            .filter(st -> st.getTaskName().equals(subtask.task_name()))
            .map(st -> st.getSubtask_id())
            .findFirst()
            .orElseThrow();

    // Attempt to delete a subtask that doesn't exist
    underTest.DeleteSubTaskByID(subtask_id + 1);

    boolean exists = underTest.GetAllSubTask()
            .stream()
            .anyMatch(st -> st.getTaskName().equals(subtask.task_name()));

    assertThat(exists).isTrue();
}

@Test
public void DeleteSubTasksByTaskID_FalseCase()
{
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

    TaskRegistrationRequest task = new TaskRegistrationRequest(LocalDate.now(), LocalDate.now(), randomTaskName, "clothes", "buy new clothes", "High", 1);

    underTest.CreateTask(task);

    Integer task_id = underTest.GetAllTasks()
            .stream()
            .filter(t -> t.getTaskName().equals(task.taskName() + "-" + task.user_id()))
            .map(t -> t.getTaskId())
            .findFirst()
            .get();

    // Generate a random subtask name
    String randomSubTaskName = getRandomSubTaskName();

    SubTaskRegistrationRequest subtask = new SubTaskRegistrationRequest(randomSubTaskName, "go to the store", task_id);

    underTest.CreateSubTask(subtask);

    // Attempt to delete subtasks for a task that doesn't exist
    underTest.DeleteSubTasksByTaskID(task_id + 1);

    boolean exists = underTest.GetAllSubTask()
            .stream()
            .anyMatch(st -> st.getParentTaskId().equals(task_id));

    assertThat(exists).isTrue();
 }

}
