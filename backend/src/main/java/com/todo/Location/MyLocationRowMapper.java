package com.todo.Location;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class MyLocationRowMapper implements RowMapper<MyLocation>
{  
    @Override
    public MyLocation mapRow(ResultSet rs, int rowNum) throws SQLException {
        MyLocation location = new MyLocation();
        location.setLongitude(rs.getFloat("longitude"));
        location.setLatitude(rs.getFloat("latitude"));
        location.setLocation_id(rs.getInt("location_id"));
        location.setTime_zone(rs.getString("time_zone"));
        location.setCity_name(rs.getString("city_name"));
        return location;
    }
}
