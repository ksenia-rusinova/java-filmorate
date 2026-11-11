package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    private final Map<Long, Film> films = new HashMap<>();
    private static final LocalDate FIRST_RELEASE_DATE =
            LocalDate.of(1895, 12, 28);

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film postFilm(@RequestBody Film film) {
        nameEx(film);
        descriptionEx(film);
        releaseDateEx(film);
        durationEx(film);

        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("film добавлен в HashMap films, id = {}", film.getId());

        return film;
    }

    @PutMapping
    public Film putFilm(@RequestBody Film film) {
        nameEx(film);
        descriptionEx(film);
        releaseDateEx(film);
        durationEx(film);

        if(films.containsKey(film.getId())){
            films.put(film.getId(), film);
            log.info("film обновлен в HashMap films по id = {}", film.getId());
        } else {
            log.error("film НЕ найден в HashMap films по id = {}", film.getId());
        }

        return film;
    }

    private Long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void nameEx(Film film) {
        if(film.getName() == null
                || film.getName().isBlank()){
            log.warn("Название фильма = {}, сгенерировано исключение ValidationException", film.getName());
            throw new ValidationException("Название фильма НЕ может быть пустым");
        }
    }

    private void descriptionEx(Film film) {
        if(film.getDescription() != null
                && film.getDescription().length() > 200){
            log.warn("Описание = {}, сгенерировано исключение ValidationException", film.getDescription());
            throw new ValidationException("Максимальная длина описания 200 символов");
        }
    }

    private void releaseDateEx(Film film) {
        if (film.getReleaseDate() != null
                && film.getReleaseDate().isBefore(FIRST_RELEASE_DATE)) {
            log.warn("Дата релиза = {}, сгенерировано исключение ValidationException", film.getReleaseDate());
            throw new ValidationException(
                    "Дата релиза не может быть раньше 28 декабря 1895 года"
            );
        }
    }

    private void durationEx(Film film) {
        if (film.getDuration() != null
                && film.getDuration() <= 0) {
            log.warn("Продолжительность фильма = {}, сгенерировано исключение ValidationException", film.getDuration());
            throw new ValidationException(
                    "Продолжительность фильма должна != 0 и быть положительным числом"
            );
        }
    }
}
