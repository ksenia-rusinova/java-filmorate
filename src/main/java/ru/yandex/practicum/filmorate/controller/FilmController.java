package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private static final LocalDate FIRST_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private long nextId = 1;

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film postFilm(@RequestBody Film film) {
        validate(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("film добавлен в HashMap films, id = {}", film.getId());
        return film;
    }

    @PutMapping
    public Film putFilm(@RequestBody Film film) {
        if (film.getId() == null || !films.containsKey(film.getId())) {
            log.error("film НЕ найден в HashMap films по id = {}", film.getId());
            throw new ValidationException(HttpStatus.NOT_FOUND, "id = null или НЕ найден в HashMap films");
        }

        validate(film);
        films.put(film.getId(), film);
        log.info("film обновлен в HashMap films по id = {}", film.getId());
        return film;
    }

    private Long getNextId() {
        return nextId++;
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Название фильма = {}, сгенерировано исключение ValidationException", film.getName());
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Название фильма НЕ может быть пустым");
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.warn("Описание = {}, сгенерировано исключение ValidationException", film.getDescription());
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Максимальная длина описания 200 символов");
        }

        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(FIRST_RELEASE_DATE)) {
            log.warn("Дата релиза = {}, сгенерировано исключение ValidationException", film.getReleaseDate());
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Дата релиза не может быть раньше 28 декабря 1895 года");
        }

        if (film.getDuration() != null && film.getDuration() <= 0) {
            log.warn("Продолжительность фильма = {}, сгенерировано исключение ValidationException", film.getDuration());
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Продолжительность фильма должна != 0 и быть положительным числом");
        }
    }
}
