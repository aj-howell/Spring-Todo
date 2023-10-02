package com.todo;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.todo.Location.MyLocation;
import com.todo.Location.MyLocationRegistrationRequest;
import com.todo.Location.MyLocationRowMapper;

public class JDBCLocationTest extends AbstractTestcontainersUnitTests
{
    private JDBCAccessTemplate underTest;
    private MyLocationRowMapper locationMapper;
    
    @BeforeEach
    void setup()
    {
        locationMapper = new MyLocationRowMapper();
        underTest = new JDBCAccessTemplate(getJDBC(), null, null, null, locationMapper);
    }

    @Test
    public void testGetLocationById() {
                MyLocationRegistrationRequest locationRequest = new MyLocationRegistrationRequest(
                123.456f,
                78.910f,
                "Eastern Time",
                "new city"
        );
        underTest.CreateLocation(locationRequest);

        Integer location_id= underTest.GetAllLocations()
        .stream()
        .filter(lc->lc.getCity_name() != null && lc.getCity_name().equals(locationRequest.city_name()))
        .map(lc->lc.getLocation_id())
        .findFirst()
        .orElseThrow();

        Optional<MyLocation> location = underTest.GetLocationById(location_id);

        assertThat(location).isPresent()
        .hasValueSatisfying(selected ->
        {
            assertThat(selected.getLatitude()).isEqualTo(locationRequest.latitude());
            assertThat(selected.getLongitude()).isEqualTo(locationRequest.longitude());
            assertThat(selected.getCity_name()).isEqualTo(locationRequest.city_name());
            assertThat(selected.getTime_zone()).isEqualTo(locationRequest.time_zone());
        });
    }

    @Test
    public void testGetLocationById_LocationNotFound() {
        boolean exists=false;

        try {
            exists= underTest.GetAllLocations()
             .stream()
             .anyMatch(t->t.getLocation_id()==-1);
        } catch (Exception e) {
            assertThat(exists).isFalse();
        }

    }

    @Test
    public void testGetAllLocations() {
        MyLocationRegistrationRequest locationRequest = new MyLocationRegistrationRequest(
                123.456f,
                78.910f,
                "Eastern Time",
                "Boston"
        );
        underTest.CreateLocation(locationRequest);

        List<MyLocation> locations = underTest.GetAllLocations();

        assertThat(locations).isNotEmpty();
    }

    @Test
    public void testCreateLocation() {
        MyLocationRegistrationRequest locationRequest = new MyLocationRegistrationRequest(
                123.456f,
                78.910f,
                "Eastern Time",
                "new city"
        );
        underTest.CreateLocation(locationRequest);

       Optional<MyLocation> location= underTest.GetAllLocations()
        .stream()
        .filter(lc->lc.getCity_name() != null && lc.getCity_name().equals(locationRequest.city_name()))
        .findFirst();

        assertThat(location).isPresent()
        .hasValueSatisfying(selected ->
        {
            assertThat(selected.getLatitude()).isEqualTo(locationRequest.latitude());
            assertThat(selected.getLongitude()).isEqualTo(locationRequest.longitude());
            assertThat(selected.getCity_name()).isEqualTo(locationRequest.city_name());
            assertThat(selected.getTime_zone()).isEqualTo(locationRequest.time_zone());
        });

    }
}
