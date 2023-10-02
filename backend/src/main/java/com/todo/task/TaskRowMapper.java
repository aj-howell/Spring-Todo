package com.todo.task;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskRowMapper implements RowMapper<Task>
{

    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        Task task = new Task();
        task.setTaskId(rs.getInt("task_id"));
        task.setUser_id(rs.getInt("user_id"));
        task.setTaskName(rs.getString("name"));
        task.setTopic(rs.getString("topic"));
        task.setDescription(rs.getString("description"));
        task.setPriority(rs.getString("priority"));
        task.setDueDate(rs.getDate("due_date").toLocalDate());
        task.setStartDate(rs.getDate("start_date").toLocalDate());

        return task;
    }
    
}
