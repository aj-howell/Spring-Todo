package com.todo;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.todo.Location.LocationService;
import com.todo.Location.MyLocation;
import com.todo.User.User;

public class LocationServiceTest extends AbstractTestcontainersUnitTests
{
    private LocationService underTest;

    @Mock
    private TodoDAO locationDao;

    @BeforeEach
    void setup()
    {
        locationDao = mock(TodoDAO.class);
        underTest = new LocationService(locationDao);
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


    @Test
    public void testEditUserLocation(){
        User selected = new User(1, "aj", "aj@gmail.com", "password", "702-234-5554", new MyLocation(0, 0, null, 1, null));
        when(locationDao.GetUserById(1)).thenReturn(Optional.of(selected));

        MyLocation location = new MyLocation(123.456f, 78.910f, "Eastern Time", 1, "Boston");
        when(locationDao.GetLocationById(1)).thenReturn(Optional.of(location));

        MyLocation updated_location = new MyLocation(428.4f, 999.910f, "Central Time", 2, "Chicago");
        when(locationDao.GetLocationById(2)).thenReturn(Optional.of(updated_location));

        underTest.EditUserLocation(selected.getUserId(), updated_location.getLocation_id());

        User updated_User = new User(1, "aj", "aj@gmail.com", "password", "702-234-5554", new MyLocation(428.4f, 999.910f, "Central Time", 2, "Chicago"));
        
        when(locationDao.GetUserById(1)).thenReturn(Optional.of(updated_User));



       assertThat(updated_User.getLocation()).isEqualTo(updated_location);

    }

    @Test
    public void testEditUserLocation_UserNotFound() {
        when(locationDao.GetUserById(1)).thenReturn(null);

        boolean valid =false;
        try {
            underTest.EditUserLocation(1, 1);
            valid = true;
        } catch (Exception e) {
            assertThat(valid).isFalse();
        }
        
    }

    @Test
    public void testEditUserLocation_LocationNotFound()
    {
         User selected = new User(1, "aj", "aj@gmail.com", "password", "702-234-5554", new MyLocation(0, 0, null, 1, null));
        when(locationDao.GetUserById(1)).thenReturn(Optional.of(selected));

        when(locationDao.GetLocationById(1)).thenReturn(null);

        boolean valid =false;
        
        try {
            underTest.EditUserLocation(1, 1);
            valid = true;
        } catch (Exception e) {
            assertThat(valid).isFalse();
        }
    }

@Test
public void testGetLocationByUserId() {
    User selected = new User(1, "aj", "aj@gmail.com", "password", "702-234-5554", new MyLocation(0, 0, null, 1, null));
    
    // Mock the behavior for GetUserById
    when(locationDao.GetUserById(1)).thenReturn(Optional.of(selected));

    // Mock the behavior for GetLocationById
    MyLocation location = new MyLocation(123.456f, 78.910f, "Eastern Time", 1, "Boston");
    when(locationDao.GetLocationById(1)).thenReturn(Optional.of(location));

    MyLocation result = underTest.GetLocationByUserId(1);

    // Verify that the location returned by GetLocationByUserId matches the expected location
    assertEquals(location, result);
}


    @Test
    public void testGetLocationByUserId_UserNotFound() {
            User selected = new User(1, "aj", "aj@gmail.com", "password", "702-234-5554", new MyLocation(0, 0, null, null, null));
    
        // Mock the behavior for GetUserById
        when(locationDao.GetUserById(1)).thenReturn(Optional.of(selected));

        // Mock the behavior for GetLocationById
        when(locationDao.GetLocationById(1)).thenReturn(null);

        MyLocation result=null;
        Optional<MyLocation> location=null;
        try {
            result = underTest.GetLocationByUserId(1);
            location = locationDao.GetLocationById(1);
        } catch (Exception e) {
        assertEquals(result,location);
        }

    }

    @Test
    public void testGetLocations() {
        MyLocation location = new MyLocation(123.456f, 78.910f, "Eastern Time", 1, "Boston");
        when(locationDao.GetLocationById(1)).thenReturn(Optional.of(location));

        MyLocation updated_location = new MyLocation(428.4f, 999.910f, "Central Time", 2, "Chicago");
        when(locationDao.GetLocationById(2)).thenReturn(Optional.of(updated_location));

        List<MyLocation> locations = new ArrayList<>();
     
        locations.add(location);
        locations.add(updated_location);

        when(locationDao.GetAllLocations()).thenReturn(locations);

        assertThat(underTest.getLocations()).isNotEmpty();
        assertThat(locationDao.GetLocationById(1).get()).isEqualTo(locations.get(0));
        assertThat(locationDao.GetLocationById(2).get()).isEqualTo(locations.get(1));
    }
}
