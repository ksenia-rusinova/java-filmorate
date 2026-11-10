package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private static final LocalDate TODAY = LocalDate.now();

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User postUser(@RequestBody User user) {
        emailEx(user);
        loginEx(user);
        birthdayEx(user);

        user.setId(getNextId());
        checkIfUserNameEqNull(user);

        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User putUser(@RequestBody User user) {
        emailEx(user);
        loginEx(user);
        birthdayEx(user);

        if(users.containsKey(user.getId())){
            checkIfUserNameEqNull(user);

            users.put(user.getId(), user);
        }
        return user;
    }

    private Long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void emailEx(User user) {
        if(user.getEmail() == null
                || !user.getEmail().contains("@")){
            throw new ValidationException("Электронная почта НЕ может быть пустой и должна содержать символ @");
        }
    }

    private void loginEx(User user) {
        if(user.getLogin() == null
                || user.getLogin().isBlank()){
            throw new ValidationException("Логин НЕ может быть пустым и содержать пробелы");
        }
    }

    private void checkIfUserNameEqNull(User user) {
        if(user.getName() == null){
            user.setName(user.getLogin());
        }
    }

    private void birthdayEx(User user) {
        if(user.getBirthday() != null
                && user.getBirthday().isAfter(TODAY)){
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
