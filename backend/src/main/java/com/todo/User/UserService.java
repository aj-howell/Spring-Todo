package com.todo.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.todo.TodoDAO;
import com.todo.task.Exceptions.ResourceNotFoundException;

@Service
public class UserService {
    private final TodoDAO userDao;

    public UserService(@Qualifier("jdbc")TodoDAO userDao) {
        this.userDao = userDao;
    }

    public List<User> GetUsers() {
        return userDao.GetAllUsers();
    }

    public void CreateUser(UserRegistrationRequest user) {
        userDao.CreateUser(user);
    }

    public void EditUser(Integer userId, UserRegistrationRequest user) {
        userDao.GetUserById(userId)
        .orElseThrow( ()-> new ResourceNotFoundException("This user does not exist"));
        userDao.EditUser(userId, user);
    }

    public void DeleteUser(Integer userId) {
        userDao.GetUserById(userId)
        .orElseThrow( ()-> new ResourceNotFoundException("This user does not exist"));

        userDao.DeleteUserById(userId);
    }

    public User GetUser(Integer userId) {
        return userDao.GetUserById(userId)
        .orElseThrow( ()-> new ResourceNotFoundException("This user does not exist"));
    }
}
