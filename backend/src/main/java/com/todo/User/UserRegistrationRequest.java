package com.todo.User;

public record UserRegistrationRequest(String name, String email, String password, String phoneNumber, Integer location_id)
{

}