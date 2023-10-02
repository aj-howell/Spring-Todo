package com.todo.task.Exceptions;

public class ResourceNotFoundException extends NullPointerException  {

    public ResourceNotFoundException(String message)
    {
        super(message);
    }
}
