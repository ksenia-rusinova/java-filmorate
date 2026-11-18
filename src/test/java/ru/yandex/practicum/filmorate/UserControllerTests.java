package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTests {
    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    /**
     * POST /users
     */
    @Test
    void postUser_emailNull_returnException() {
        User user = new User();
        user.setEmail(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.postUser(user));

        assertEquals("Электронная почта НЕ может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    void postUser_emailEmpty_returnException() {
        User user = new User();
        user.setEmail("");

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.postUser(user));

        assertEquals("Электронная почта НЕ может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    void postUser_emailWithoutAt_returnException() {
        User user = new User();
        user.setEmail("user.example.com");

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.postUser(user));

        assertEquals("Электронная почта НЕ может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    void postUser_loginNull_returnException() {
        User user = new User();
        user.setEmail("user.example@gmail.com");
        user.setLogin(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.postUser(user));

        assertEquals("Логин НЕ может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void postUser_loginBlank_returnException() {
        User user = new User();
        user.setEmail("user.example@gmail.com");
        user.setLogin("  ");

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.postUser(user));

        assertEquals("Логин НЕ может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void postUser_nameNull_setNameToLogin() {
        User user = new User();
        user.setEmail("user.example@gmail.com");
        user.setLogin("testLogin");
        user.setName(null);

        User result = userController.postUser(user);

        assertEquals(user.getLogin(), result.getName());
    }

    @Test
    void postUser_nameEmpty_setNameToLogin() {
        User user = new User();
        user.setEmail("user.example@gmail.com");
        user.setLogin("testLogin");
        user.setName("");

        User result = userController.postUser(user);

        assertEquals(user.getLogin(), result.getName());
    }

    @Test
    void postUser_birthdayInFuture_returnException() {
        User user = new User();
        user.setEmail("user.example@gmail.com");
        user.setLogin("testLogin");
        user.setBirthday(LocalDate.now().plusDays(1));

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.postUser(user));

        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

    @Test
    void postUser_birthdayToday_createUser() {
        User user = new User();
        user.setEmail("user.example@gmail.com");
        user.setLogin("testLogin");
        user.setBirthday(LocalDate.now());

        User result = userController.postUser(user);

        assertEquals(user.getBirthday(), result.getBirthday());
    }

    /**
     * PUT /users
     */
    private User createExistingUser() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("existingLogin");
        user.setName("Existing User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        return userController.postUser(user);
    }

    private User createValidUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setEmail("user@example.com");
        user.setLogin("testLogin");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1995, 5, 15));

        return user;
    }

    @Test
    void putUser_emailNull_returnException() {
        User existingUser = createExistingUser();
        User user = createValidUser(existingUser.getId());
        user.setEmail(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.putUser(user));

        assertEquals("Электронная почта НЕ может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    void putUser_emailEmpty_returnException() {
        User existingUser = createExistingUser();
        User user = createValidUser(existingUser.getId());
        user.setEmail("");

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.putUser(user));

        assertEquals("Электронная почта НЕ может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    void putUser_emailWithoutAt_returnException() {
        User existingUser = createExistingUser();
        User user = createValidUser(existingUser.getId());
        user.setEmail("user.example.com");

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.putUser(user));

        assertEquals("Электронная почта НЕ может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    void putUser_loginNull_returnException() {
        User existingUser = createExistingUser();
        User user = createValidUser(existingUser.getId());
        user.setLogin(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.putUser(user));

        assertEquals("Логин НЕ может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void putUser_loginBlank_returnException() {
        User existingUser = createExistingUser();
        User user = createValidUser(existingUser.getId());
        user.setLogin("  ");

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.putUser(user));

        assertEquals("Логин НЕ может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void putUser_nameNull_setNameToLogin() {
        User existingUser = createExistingUser();
        User user = createValidUser(existingUser.getId());
        user.setName(null);

        User result = userController.putUser(user);

        assertEquals(user.getLogin(), result.getName());
    }

    @Test
    void putUser_nameEmpty_setNameToLogin() {
        User existingUser = createExistingUser();
        User user = createValidUser(existingUser.getId());
        user.setName("");

        User result = userController.putUser(user);

        assertEquals(user.getLogin(), result.getName());
    }

    @Test
    void putUser_birthdayInFuture_returnException() {
        User existingUser = createExistingUser();
        User user = createValidUser(existingUser.getId());
        user.setBirthday(LocalDate.now().plusDays(1));

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.putUser(user));

        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

    @Test
    void putUser_birthdayToday_updateUser() {
        User existingUser = createExistingUser();
        User user = createValidUser(existingUser.getId());
        user.setBirthday(LocalDate.now());

        User result = userController.putUser(user);

        assertEquals(LocalDate.now(), result.getBirthday());
    }
}
