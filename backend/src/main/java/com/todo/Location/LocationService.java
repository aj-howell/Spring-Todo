package com.todo.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.todo.TodoDAO;
import com.todo.Exceptions.ResourceNotFoundException;
import com.todo.Location.MyLocation;

@Service
public class LocationService {

    private final TodoDAO locationDao;

    public LocationService(@Qualifier("jdbc") TodoDAO locationDao) {
        this.locationDao = locationDao;
    }

    public void EditUserLocation(Integer userId, Integer location_id) {
        locationDao.GetUserById(userId)
        .orElseThrow(()-> new ResourceNotFoundException("User with id: "+userId+" was not found"));

        locationDao.GetLocationById(location_id)
        .orElseThrow(()-> new ResourceNotFoundException("Location with id: "+location_id+" was not found"));

        locationDao.EditUserLocation(userId, location_id);
    }

    public MyLocation GetLocationByUserId(Integer userId) {
          User selected= locationDao.GetUserById(userId)
        .orElseThrow(()-> new ResourceNotFoundException("User with id: "+userId+" was not found"));

        //the id will be the only thing set after it is selected
        Integer location_id=selected.getLocation().getLocation_id();

        //grab the rest of the details by querying
        MyLocation userLocation=locationDao.GetLocationById(location_id).get();
        
        //set them
        selected.setLocation(userLocation);
        
       return selected.getLocation();
    }

    public List<MyLocation> getLocations() {
        return locationDao.GetAllLocations(); // Call the get all locations method from JDBCAccessTemplate
    }
}
