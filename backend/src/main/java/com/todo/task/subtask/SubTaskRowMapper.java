package com.todo.task.subtask;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class SubTaskRowMapper implements RowMapper<SubTask>
{  
    @Override
    public SubTask mapRow(ResultSet rs, int rowNum) throws SQLException {
        SubTask subtask = new SubTask();
        subtask.setSubtask_id(rs.getInt("subtask_id"));
        subtask.setTaskName(rs.getString("name"));
        subtask.setDescription(rs.getString("description"));
        subtask.setParentTaskId(rs.getInt("task_id"));
        
        return subtask;
    }
}
