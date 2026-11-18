package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User postUser(@RequestBody User user) {
        validate(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("user добавлен в HashMap users, id = {}", user.getId());
        return user;
    }

    @PutMapping
    public User putUser(@RequestBody User user) {
        if (user.getId() == null || !users.containsKey(user.getId())) {
            log.error("user НЕ найден в HashMap users по id = {}", user.getId());
            throw new ValidationException(HttpStatus.NOT_FOUND, "id = null или НЕ найден в HashMap users");
        }

        validate(user);
        users.put(user.getId(), user);
        log.info("user обновлен в HashMap users по id = {}", user.getId());
        return user;
    }

    private Long getNextId() {
        return nextId++;
    }

    private void validate(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.warn("Электронная почта = {}, сгенерировано исключение ValidationException", user.getEmail());
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Электронная почта НЕ может быть пустой и должна содержать символ @");
        }

        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("Логин = {}, сгенерировано исключение ValidationException", user.getLogin());
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Логин НЕ может быть пустым и содержать пробелы");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("если name = null или состоит из пробелов, в поле name записали значение из поля login = {}", user.getLogin());
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения = {}, сгенерировано исключение ValidationException", user.getBirthday());
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Дата рождения не может быть в будущем");
        }
    }
}
