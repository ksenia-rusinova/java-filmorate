package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

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
        log.info("user добавлен в HashMap users, id = {}", user.getId());

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
            log.info("user обновлен в HashMap users по id = {}", user.getId());
        } else {
            log.error("user НЕ найден в HashMap users по id = {}", user.getId());
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
            log.warn("Электронная почта = {}, сгенерировано исключение ValidationException", user.getEmail());
            throw new ValidationException("Электронная почта НЕ может быть пустой и должна содержать символ @");
        }
    }

    private void loginEx(User user) {
        if(user.getLogin() == null
                || user.getLogin().isBlank()){
            log.warn("Логин = {}, сгенерировано исключение ValidationException", user.getLogin());
            throw new ValidationException("Логин НЕ может быть пустым и содержать пробелы");
        }
    }

    private void checkIfUserNameEqNull(User user) {
        if(user.getName() == null){
            user.setName(user.getLogin());
            log.info("name не пришло или name = null, в поле name записали значение из поля login = {}", user.getLogin());
        }
    }

    private void birthdayEx(User user) {
        if(user.getBirthday() != null
                && user.getBirthday().isAfter(TODAY)){
            log.warn("Дата рождения = {}, сгенерировано исключение ValidationException", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
