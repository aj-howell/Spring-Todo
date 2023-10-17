package com.todo.User;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todo.Location.LocationService;
import com.todo.Location.MyLocation;
import com.todo.task.Task;


@RestController
@RequestMapping("/api/v1/users")
public class UserController
{
    private UserService userService;
    private LocationService locationService;

    public UserController(UserService userService, LocationService locationService)
    {
        this.userService=userService;
        this.locationService=locationService;
    }


    @GetMapping()
    public List<User> getUsers() {
        return userService.GetUsers();
    }

    @PostMapping()
    public void createUser(@RequestBody UserRegistrationRequest user) {
        userService.CreateUser(user);
    }

    @PutMapping("{userId}")
    public void editUser(@PathVariable("userId") Integer id, @RequestBody UserRegistrationRequest user) {
        userService.EditUser(id, user);
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable("userId") Integer userId) {
        userService.DeleteUser(userId);
    }

    @GetMapping("{userId}")
    public User getUser(@PathVariable("userId") Integer userId) {
        return userService.GetUser(userId);
    }

    //Locations
    @PutMapping("{userId}/location")
    public void editLocation(@PathVariable("userId") Integer userId, Integer new_Id) {
        locationService.EditUserLocation(userId,new_Id);
    }

    @GetMapping("{userId}/location")
    public MyLocation getLocation(@PathVariable("userId") Integer userId) {
        return locationService.GetLocationByUserId(userId);
    }

    @GetMapping("/locations")
    public List<MyLocation> getLocations() {
        return locationService.getLocations();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("{userId}/prayer")
    public Task getPrayer(@PathVariable("userId") Integer userId) {
        return userService.GetUserPrayer2(userId);
    }
}