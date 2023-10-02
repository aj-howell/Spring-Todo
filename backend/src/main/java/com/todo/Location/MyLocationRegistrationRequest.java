package com.todo.Location;

public record MyLocationRegistrationRequest(
    float longitude,
    float latitude,
    String time_zone,
    String city_name
) {

}
