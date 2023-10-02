package com.todo;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.todo.Location.MyLocation;
import com.todo.User.User;
import com.todo.User.UserRegistrationRequest;
import com.todo.User.UserService;

public class UserServiceTest extends AbstractTestcontainersUnitTests {
        private UserService underTest;

    @Mock
    private TodoDAO UserDao;

    @BeforeEach
    void setup()
    {
        UserDao = mock(TodoDAO.class);
        underTest = new UserService(UserDao);
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
    public void testGetUsers() {
        User selected = new User(1, "aj", "aj@gmail.com", "password", "702-234-5554", new MyLocation(0, 0, null, 1, "Boston"));
        List<User> users = new ArrayList<>();

        users.add(selected);

        when(UserDao.GetAllUsers()).thenReturn(users);
        

        List<User> actual = underTest.GetUsers();

        assertThat(actual).isNotEmpty();
        assertThat(actual.get(0)).isEqualTo(selected);
    }

    @Test
    public void testGetUsersFalseCase() {
        when(UserDao.GetAllUsers()).thenReturn(null);
        

        List<User> actual = underTest.GetUsers();

        assertThat(actual).isNull();
    }

    @Test
    public void testCreateUser() {        
        underTest.CreateUser(new UserRegistrationRequest("aj", "aj@gmail.com", "password", "702-234-5554", 1));
        verify(UserDao).CreateUser(new UserRegistrationRequest("aj", "aj@gmail.com", "password", "702-234-5554", 1));
    }

    @Test
    public void testCreateUserFalseCase() {
        verify(UserDao, never()).CreateUser(new UserRegistrationRequest("aj", "aj@gmail.com", "password", "702-234-5554", 1));
    }

    @Test
    public void testEditUser() {
        User selected = new User(1, "aj", "aj@gmail.com", "password", "702-234-5554", new MyLocation(0, 0, null, 1, "Boston"));
        when(UserDao.GetUserById(1)).thenReturn(Optional.of(selected));
        
        underTest.EditUser(1, new UserRegistrationRequest("aj", "aj@gmail.com", "password", "702-234-5554", 1));
        verify(UserDao).EditUser(1,new UserRegistrationRequest("aj", "aj@gmail.com", "password", "702-234-5554", 1));
    }

    @Test
    public void testEditUserFalseCase() {
         when(UserDao.GetUserById(1)).thenReturn(null);
        
        try {
            underTest.EditUser(1, new UserRegistrationRequest("aj", "aj@gmail.com", "password", "702-234-5554", 1));
        } catch (Exception e) {
            verify(UserDao, never()).EditUser(1,new UserRegistrationRequest("aj", "aj@gmail.com", "password", "702-234-5554", 1));
        }
    }

    @Test
    public void testDeleteUser() {
        User selected = new User(1, "aj", "aj@gmail.com", "password", "702-234-5554", new MyLocation(0, 0, null, 1, "Boston"));
        when(UserDao.GetUserById(1)).thenReturn(Optional.of(selected));

        underTest.DeleteUser(1);
        verify(UserDao).DeleteUserById(1);
    }

    @Test
    public void testDeleteUserFalseCase() {
        when(UserDao.GetUserById(1)).thenReturn(null);

        try {
            underTest.DeleteUser(1);
        } catch (Exception e) {
            verify(UserDao, never()).DeleteUserById(1);
        }
    }

    @Test
    public void testGetUser() {
             User selected = new User(1, "aj", "aj@gmail.com", "password", "702-234-5554", new MyLocation(0, 0, null, 1, "Boston"));
        
        underTest.CreateUser(new UserRegistrationRequest("aj", "aj@gmail.com", "password", "702-234-5554", 1));

        when(UserDao.GetUserById(1)).thenReturn(Optional.of(selected));

       User actual = underTest.GetUser(1);

        assertThat(actual).isEqualTo(selected);
    }

    @Test
    public void testGetUserFalseCase() {
        User selected = new User(1, "aj", "aj@gmail.com", "password", "702-234-5554", new MyLocation(0, 0, null, 1, "Boston"));

        underTest.CreateUser(new UserRegistrationRequest("aj", "aj@gmail.com", "password", "702-234-5554", 1));

        when(UserDao.GetUserById(1)).thenReturn(null);

        User actual = null;

        try {
           actual= underTest.GetUser(1);
        } catch (Exception e) {
           assertThat(actual).isNotEqualTo(selected);
        }
    }
}
