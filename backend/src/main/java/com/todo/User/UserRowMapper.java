package com.todo.User;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.todo.Location.MyLocation;

@Component
public class UserRowMapper implements RowMapper<User>
{  
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhoneNumber(rs.getString("phone_number"));
        MyLocation location = new MyLocation();
        location.setLocation_id(rs.getInt("location_id"));
        user.setLocation(location);
        return user;
    }
}

