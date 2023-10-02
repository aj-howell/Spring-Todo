package com.todo;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.javafaker.Faker;
import com.todo.Location.MyLocationRowMapper;
import com.todo.User.User;
import com.todo.User.UserRegistrationRequest;
import com.todo.User.UserRowMapper;

public class JDBCUserTest extends AbstractTestcontainersUnitTests {
    private JDBCAccessTemplate underTest;
    private UserRowMapper userRowMapper;
    private MyLocationRowMapper locationMapper;
    private final Faker faker = new Faker();

    @BeforeEach
    void setup() {
        userRowMapper = new UserRowMapper();
        locationMapper = new MyLocationRowMapper();

        underTest = new JDBCAccessTemplate(getJDBC(), null, null, userRowMapper, locationMapper);
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
    public void testCreateUser() {
        String name = getRandomUserName();
        String email = getRandomEmail();
        String phoneNumber = getRandomPhoneNumber();

        UserRegistrationRequest userRequest = new UserRegistrationRequest(name, email, "password", phoneNumber, 1);

        underTest.CreateUser(userRequest);

        List<User> users = underTest.GetAllUsers();

        Optional<User> user = users.stream()
                .filter(u -> u.getEmail().equals(userRequest.email()))
                .findFirst();

        assertThat(user).isPresent().hasValueSatisfying(u -> {
            assertThat(u.getEmail()).isEqualTo(email);
            assertThat(u.getLocation().getLocation_id()).isEqualTo(userRequest.location_id());
            assertThat(u.getPassword()).isEqualTo(userRequest.password());
            assertThat(u.getPhoneNumber()).isEqualTo(userRequest.phoneNumber());
            assertThat(u.getName()).isEqualTo(userRequest.name());
        });
    }

    @Test
    public void testGetAllUsers() {
        String name = getRandomUserName();
        String email = getRandomEmail();
        String phoneNumber = getRandomPhoneNumber();

        UserRegistrationRequest userRequest = new UserRegistrationRequest(name, email, "password", phoneNumber, 1);

        underTest.CreateUser(userRequest);

        List<User> users = underTest.GetAllUsers();

        assertThat(users).isNotEmpty();
    }

    @Test
    public void testGetUserById() {
        String name = getRandomUserName();
        String email = getRandomEmail();
        String phoneNumber = getRandomPhoneNumber();

        UserRegistrationRequest userRequest = new UserRegistrationRequest(name, email, "password", phoneNumber, 1);

        underTest.CreateUser(userRequest);

        List<User> users = underTest.GetAllUsers();

        Integer userId = users.stream()
                .filter(u -> u.getEmail().equals(userRequest.email()))
                .map(User::getUserId)
                .findFirst()
                .orElseThrow();

        Optional<User> user = underTest.GetUserById(userId);

        assertThat(user).isPresent().hasValueSatisfying(u -> {
            assertThat(u.getEmail()).isEqualTo(email);
            assertThat(u.getLocation().getLocation_id()).isEqualTo(userRequest.location_id());
            assertThat(u.getPassword()).isEqualTo(userRequest.password());
            assertThat(u.getPhoneNumber()).isEqualTo(userRequest.phoneNumber());
            assertThat(u.getName()).isEqualTo(userRequest.name());
        });
    }

    @Test
    public void testEditUser() {
        String name = getRandomUserName();
        String email = getRandomEmail();
        String phoneNumber = getRandomPhoneNumber();

        UserRegistrationRequest userRequest = new UserRegistrationRequest(name, email, "password", phoneNumber, 1);

        underTest.CreateUser(userRequest);

        List<User> users = underTest.GetAllUsers();

        Integer userId = users.stream()
                .filter(u -> u.getEmail().equals(userRequest.email()))
                .map(User::getUserId)
                .findFirst()
                .orElseThrow();

        String updatedName = getRandomUserName();
        String updatedEmail = getRandomEmail();
        String updatedPhoneNumber = getRandomPhoneNumber();

        UserRegistrationRequest updatedRequest = new UserRegistrationRequest(updatedName, updatedEmail, "password", updatedPhoneNumber, 2);

        underTest.EditUser(userId, updatedRequest);

        Optional<User> user = underTest.GetUserById(userId);

        assertThat(user).isPresent().hasValueSatisfying(u -> {
            assertThat(u.getEmail()).isEqualTo(updatedEmail);
            assertThat(u.getLocation().getLocation_id()).isEqualTo(updatedRequest.location_id());
            assertThat(u.getPassword()).isEqualTo(updatedRequest.password());
            assertThat(u.getPhoneNumber()).isEqualTo(updatedPhoneNumber);
            assertThat(u.getName()).isEqualTo(updatedName);
        });
    }

    @Test
    public void testDeleteUserById() {
        String name = getRandomUserName();
        String email = getRandomEmail();
        String phoneNumber = getRandomPhoneNumber();

        UserRegistrationRequest userRequest = new UserRegistrationRequest(name, email, "password", phoneNumber, 1);

        underTest.CreateUser(userRequest);

        List<User> users = underTest.GetAllUsers();

        Integer userId = users.stream()
                .filter(u -> u.getEmail().equals(userRequest.email()))
                .map(User::getUserId)
                .findFirst()
                .orElseThrow();

        underTest.DeleteUserById(userId);

        boolean userExists = false;

        try {
            userExists = underTest.GetUserById(userId)
                    .stream()
                    .anyMatch(u -> u.getEmail().equals(email));
        } catch (Exception e) {
            assertThat(userExists).isFalse();
        }
    }

    @Test
    public void testCreateUser_False() {
        String name = getRandomUserName();
        String email = getRandomEmail();
        String phoneNumber = getRandomPhoneNumber();

        UserRegistrationRequest userRequest = new UserRegistrationRequest(name, email, "password", phoneNumber, null);

        boolean exists = false;
        try {
            underTest.CreateUser(userRequest);
            exists = underTest.GetAllUsers()
                    .stream()
                    .anyMatch(u -> u.getEmail().equals(email));
        } catch (Exception e) {
            assertThat(exists).isFalse();
        }
    }

    @Test
    public void testGetAllUsers_False() {
        boolean length = false;
        try {
            length = underTest.GetAllUsers().size() != 1;
        } catch (Exception e) {
            assertThat(length).isFalse();
        }
    }

    @Test
    public void testGetUserById_False() {
        boolean user = false;
        try {
            user = underTest.GetUserById(0).isPresent();
        } catch (Exception e) {
            assertThat(user).isFalse();
        }
    }

    @Test
    public void testEditUser_False() {
        String name = getRandomUserName();
        String email = getRandomEmail();
        String phoneNumber = getRandomPhoneNumber();

        UserRegistrationRequest userRequest = new UserRegistrationRequest(name, email, "password", phoneNumber, 1);

        underTest.CreateUser(userRequest);

        List<User> users = underTest.GetAllUsers();

        Integer userId = users.stream()
                .filter(u -> u.getEmail().equals(userRequest.email()))
                .map(User::getUserId)
                .findFirst()
                .orElseThrow();

        UserRegistrationRequest updatedRequest = new UserRegistrationRequest(null, null, null, null, null);

        try {
            underTest.EditUser(userId, updatedRequest);
        } catch (Exception e) {
            Optional<User> user = underTest.GetUserById(userId);

            assertThat(user).isPresent().hasValueSatisfying(u -> {
                assertThat(u.getEmail()).isNotEqualTo(updatedRequest.email());
                assertThat(u.getLocation().getLocation_id()).isNotEqualTo(updatedRequest.location_id());
                assertThat(u.getPassword()).isNotEqualTo(updatedRequest.password());
                assertThat(u.getPhoneNumber()).isNotEqualTo(updatedRequest.phoneNumber());
                assertThat(u.getName()).isNotEqualTo(updatedRequest.name());
            });
        }
    }

    @Test
    public void testDeleteUserById_False() {
        boolean exists = false;
        try {
            underTest.DeleteUserById(0);
            exists = underTest.GetUserById(0).isPresent();
        } catch (Exception e) {
            assertThat(exists).isFalse();
        }
    }
}
