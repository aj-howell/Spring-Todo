package com.todo;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.todo.Location.MyLocation;
import com.todo.Location.MyLocationRegistrationRequest;
import com.todo.Location.MyLocationRowMapper;
import com.todo.User.User;
import com.todo.User.UserRegistrationRequest;
import com.todo.User.UserRowMapper;
import com.todo.task.Task;
import com.todo.task.TaskRegistrationRequest;
import com.todo.task.TaskRowMapper;
import com.todo.task.subtask.SubTask;
import com.todo.task.subtask.SubTaskRegistrationRequest;
import com.todo.task.subtask.SubTaskRowMapper;

@Repository("jdbc")
public class JDBCAccessTemplate implements TodoDAO
{
    private final JdbcTemplate jdbcTemplate;
    private final TaskRowMapper taskRowMapper;
    private final SubTaskRowMapper subTaskRowMapper;
    private final UserRowMapper userRowMapper;
    private final MyLocationRowMapper locationRowMapper;

    public JDBCAccessTemplate(JdbcTemplate jdbcTemplate, TaskRowMapper taskRowMapper, SubTaskRowMapper subTaskRowMapper, UserRowMapper userRowMapper,MyLocationRowMapper locationRowMapper)
    {
        this.jdbcTemplate=jdbcTemplate;
        this.taskRowMapper=taskRowMapper;
        this.subTaskRowMapper=subTaskRowMapper;
        this.userRowMapper = userRowMapper;
        this.locationRowMapper=locationRowMapper;
    }

    @Override
    public List<Task> GetAllTasks() {
        var sql = """
                SELECT * FROM tasks;
                """;
      
       List<Task> tasks= jdbcTemplate.query(sql,  taskRowMapper);
      tasks.forEach(t->
        {
            t.setSubTasks(
                GetSubTaskByTaskId(t.getTaskId()));
            t.getSubTasks().forEach(st->
            {
                st.setParentTaskId(t.getTaskId());
            });
     });

        return tasks;
    }

    @Override
    public List<Task> GetTasksByUserID(Integer userId)
    {
           var sql = """
                SELECT * FROM tasks WHERE user_id = ?;
                """;

            List<Task> tasks=  jdbcTemplate.query(sql,  taskRowMapper, userId);
            tasks.forEach(t->
                {
                    t.setSubTasks(
                        GetSubTaskByTaskId(t.getTaskId()));
                    t.getSubTasks().forEach(st->
                    {
                        st.setParentTaskId(t.getTaskId());
                    });
                });

            
           return  tasks;
    }


    @Override
    public Optional<Task> GetTaskByName(String task_name)
    {
           var sql = """
                SELECT * FROM tasks WHERE task_name = ?;
                """;
           return  jdbcTemplate.query(sql,  taskRowMapper, task_name)
           .stream()
           .findFirst();
    }

    @Override
    public void CreateTask(TaskRegistrationRequest task) {
        var sql = """
                INSERT INTO tasks
                (name,
                topic,
                description,
                due_date,
                start_date,
                user_id,
                priority)
                VALUES(?,?,?,?,?,?,?);
                """;
        String uniqueTaskName = task.taskName()+"-"+task.user_id();
        
        jdbcTemplate.update(sql, uniqueTaskName, task.topic(), task.description(), task.dueDate(), task.startDate(), task.user_id(), task.priority());
}

    @Override
    public void EditTask(Integer task_id, TaskRegistrationRequest task) {

          if(!task.taskName().equals(null))
        {
            var sql ="""
                        UPDATE tasks SET name=? WHERE task_id =?;
                    """;
            System.out.println("task: "+task_id+" name has been changed to "+task.taskName());
            jdbcTemplate.update(sql,task.taskName()+"-"+task.user_id(), task_id);
        }

        if(!task.topic().equals(null))
        {
            var sql ="""
                        UPDATE tasks SET topic=? WHERE task_id =?;
                    """;
            System.out.println("task: "+task_id+" topic has been changed to "+task.topic());
            jdbcTemplate.update(sql,task.topic(), task_id);
        }

       if(!task.description().equals(null))
        {
            var sql ="""
                        UPDATE tasks SET description=? WHERE task_id =?;
                    """;
            
            System.out.println("task: "+task_id+" description has been changed to "+task.description());
            jdbcTemplate.update(sql,task.description(), task_id);
        }

       if(task.dueDate() != null)
        {
            var sql ="""
                        UPDATE tasks SET due_date=? WHERE task_id =?;
                    """;
            System.out.println("task: "+task_id+" due date has been changed to "+task.dueDate());
            jdbcTemplate.update(sql,task.dueDate(), task_id);
        }

        if(task.startDate() != null)
        {
            var sql ="""
                        UPDATE tasks SET start_date = ? WHERE task_id =?;
                    """;
            System.out.println("task: "+task_id+" start date has been changed to "+task.startDate());
            jdbcTemplate.update(sql,task.startDate(), task_id);
        }

        if(!task.priority().equals(null))
        {
            var sql ="""
                        UPDATE tasks SET priority = ? WHERE task_id =?;
                    """;
            System.out.println("task: "+task_id+" priority has been changed to "+task.priority());
            jdbcTemplate.update(sql, task.priority(), task_id);
        }
    }

    @Override
    public void DeleteTaskByID(Integer taskId) {
        var sql = """
                DELETE FROM tasks WHERE task_id = ?;
                """;

        jdbcTemplate.update(sql, taskId);
    }

    @Override
    public Optional<Task> GetTaskById(Integer taskId) {
            var sql = """
                SELECT task_id,
                name,
                topic,
                description,
                due_date,
                start_date,
                user_id,
                priority FROM tasks WHERE task_id = ?;
                """;
        
       Optional<Task> selected= jdbcTemplate.query(sql,taskRowMapper,taskId)
        .stream()
        .findFirst();

        selected.get().setSubTasks(GetSubTaskByTaskId(taskId));
        selected.get().getSubTasks().forEach(st->
            {
                st.setParentTaskId(taskId);
            });

        return selected;
    }

    @Override
    public boolean CheckIfTaskExists(Integer taskId) {
        var sql = """
                SELECT COUNT(*) FROM tasks WHERE task_id = ?;
                """;
        
      return jdbcTemplate.queryForObject(sql,Integer.class,taskId) == 1;
    }


    //Subtask queries
    @Override
    public List<SubTask> GetAllSubTask() {
            var sql = """
                SELECT *
                FROM subtasks
                ORDER BY subtask_id DESC
                """;
        
        return jdbcTemplate.query(sql,subTaskRowMapper);
    }


    @Override
    public Optional<SubTask> GetSubTask(Integer subTaskId) {
            var sql = """
                SELECT subtask_id,
                name,
                description,
                task_id
                FROM subtasks WHERE subtask_id = ?
                ;
                """;
        
        return jdbcTemplate.query(sql,subTaskRowMapper,subTaskId)
        .stream()
        .findFirst();
    }

    @Override
    public List<SubTask> GetSubTaskByTaskId(Integer taskId) {
            var sql = """
                SELECT subtask_id,
                name,
                description,
                task_id
                FROM subtasks WHERE task_id = ?
                ORDER BY subtask_id DESC;
                """;
        
        return jdbcTemplate.query(sql,subTaskRowMapper,taskId);
    }


    @Override
    public void DeleteSubTaskByID(Integer subtaskId) {
          var sql = """
                DELETE FROM subtasks WHERE subtask_id = ?;
                """;

        jdbcTemplate.update(sql, subtaskId);
    }

    @Override
    public void DeleteSubTasksByTaskID(Integer taskId) {
        var sql = """
                DELETE FROM subtasks WHERE task_id = ?;
                """;

        jdbcTemplate.update(sql, taskId);
    }

    @Override
    public void CreateSubTask(SubTaskRegistrationRequest subtask) {
                var sql = """
                INSERT INTO subtasks
                (name,
                description,
                task_id)
                VALUES(?,?,?);
                """;
        jdbcTemplate.update(sql, subtask.task_name(),subtask.description(), subtask.task_id());
    }

    //Users
    @Override
    public void CreateUser(UserRegistrationRequest user) {
        var sql = """
                INSERT INTO users
                (name,
                email,
                password,
                phone_number,
                location_id) 
                VALUES(?,?,?,?,?);
                """;
        jdbcTemplate.update(
                sql,
                user.name(),
                user.email(),
                user.password(),
                user.phoneNumber(),
                user.location_id()
        );
    }

    @Override
    public List<User> GetAllUsers() {
        var sql = """
                SELECT * FROM users;
                """;
        List<User> users = jdbcTemplate.query(sql, userRowMapper);

        // Assuming you also want to fetch associated location data
        
        users.forEach(u ->
        {
            Integer location_id =u.getLocation().getLocation_id();
            MyLocation populatedLocation=GetLocationById(location_id).get();
            u.setLocation(populatedLocation);
        });

        return users;
    }

     @Override
    public Optional<User> GetUserById(Integer userId) {
        var sql = """
                SELECT *
                FROM users
                WHERE user_id = ?;
                """;

       Optional<User> selected =jdbcTemplate.query(sql, userRowMapper, userId)
                .stream()
                .findFirst();

        Integer location_id =selected.get().getLocation().getLocation_id();
        MyLocation populatedLocation=GetLocationById(location_id).get();



        selected.get().setLocation(populatedLocation);
        
        return selected;
    }

    @Override
    public void EditUser(Integer userId, UserRegistrationRequest user) {
        if (user.name() != null) {
            var sql = "UPDATE users SET name=? WHERE user_id=?";
            System.out.println("User: " + userId + " name has been changed to " + user.name());
            jdbcTemplate.update(sql, user.name(), userId);
        }
    
        if (user.email() != null) {
            var sql = "UPDATE users SET email=? WHERE user_id=?";
            System.out.println("User: " + userId + " email has been changed to " + user.email());
            jdbcTemplate.update(sql, user.email(), userId);
        }
    
        if (user.password() != null) {
            var sql = "UPDATE users SET password=? WHERE user_id=?";
            System.out.println("User: " + userId + " password has been changed to " + user.password());
            jdbcTemplate.update(sql, user.password(), userId);
        }
    
        if (user.phoneNumber() != null) {
            var sql = "UPDATE users SET  phone_number=? WHERE user_id=?";
            System.out.println("User: " + userId + "  phone_number has been changed to " + user.phoneNumber());
            jdbcTemplate.update(sql, user.phoneNumber(), userId);
        }
    
        if (user.location_id() != null) {
            var sql = "UPDATE users SET location_id=? WHERE user_id=?";
            System.out.println("User: " + userId + " location_id has been changed to " + user.location_id());
            jdbcTemplate.update(sql, user.location_id(), userId);
        }
    }

    @Override
    public void EditUserLocation(Integer userId, Integer location_id) {
            var sql = "UPDATE users SET location_id=? WHERE user_id=?";
            System.out.println("User: " + userId + " location_id has been changed to " + location_id);
            jdbcTemplate.update(sql, location_id, userId);
    }
    @Override
    public void DeleteUserById(Integer userId) {
    //    // update the location to its default value
    //     var updateLocationSql = "UPDATE users SET location_id = 0 WHERE user_id = ?";
    //     jdbcTemplate.update(updateLocationSql, userId);
    
        // Now, delete the user
        var deleteUserSql = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(deleteUserSql, userId);
    }

    //Locations
    @Override
    public Optional<MyLocation> GetLocationById(Integer locationId) {
    var sql = """
            SELECT *
            FROM locations WHERE location_id = ?
            """;
    
    return jdbcTemplate.query(sql, locationRowMapper, locationId)
            .stream()
            .findFirst();
}

    @Override
    //TODO: Make sure when these methods are called that Users still have their locations mapped when returned
    public List<MyLocation> GetAllLocations() {
        String sql = "SELECT * FROM Locations";
        return jdbcTemplate.query(sql, locationRowMapper);
    }

    @Override
    public void CreateLocation(MyLocationRegistrationRequest location) {

        String sql = "INSERT INTO Locations (longitude, latitude, time_zone, city_name) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, location.longitude(), location.latitude(), location.time_zone(), location.city_name());
    }

    @Override
public void UpdateLocation(Integer locationId, MyLocationRegistrationRequest location) {
    if (location.longitude() != 0) {
        var sql = "UPDATE Locations SET longitude=? WHERE location_id=?";
        System.out.println("Location: " + locationId + " longitude has been changed to " + location.longitude());
        jdbcTemplate.update(sql, location.longitude(), locationId);
    }

    if (location.latitude() != 0) {
        var sql = "UPDATE Locations SET latitude=? WHERE location_id=?";
        System.out.println("Location: " + locationId + " latitude has been changed to " + location.latitude());
        jdbcTemplate.update(sql, location.latitude(), locationId);
    }

    if (location.time_zone() != null) {
        var sql = "UPDATE Locations SET time_zone=? WHERE location_id=?";
        System.out.println("Location: " + locationId + " time_zone has been changed to " + location.time_zone());
        jdbcTemplate.update(sql, location.time_zone(), locationId);
    }

    if (location.city_name() != null) {
        var sql = "UPDATE Locations SET city_name=? WHERE location_id=?";
        System.out.println("Location: " + locationId + " name has been changed to " + location.city_name());
        jdbcTemplate.update(sql, location.city_name(), locationId);
    }
}
    
}
