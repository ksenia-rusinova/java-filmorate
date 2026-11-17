package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTests {
    private FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    /**
     * POST /films
     */
    @Test
    void postFilm_nameNull_returnException() {
        Film film = new Film();
        film.setName(null);
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1900, 3, 25));
        film.setDuration(200);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.postFilm(film));

        assertEquals("Название фильма НЕ может быть пустым", exception.getMessage());
    }

    //description = 201 символ
    @Test
    void postFilm_descriptionLongerThan200_returnException() {
        Film film = new Film();
        film.setName("Film name");
        film.setDescription("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901");
        film.setReleaseDate(LocalDate.of(1900, 3, 25));
        film.setDuration(200);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.postFilm(film));

        assertEquals("Максимальная длина описания 200 символов", exception.getMessage());
    }

    //description = 200 символов
    @Test
    void postFilm_description200_filmAdded() {
        Film film = new Film();
        film.setName("Film name");
        film.setDescription("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        film.setReleaseDate(LocalDate.of(1967, 3, 25));
        film.setDuration(100);

        Film rs = filmController.postFilm(film);

        assertNotNull(rs.getId());
        assertEquals("Film name", rs.getName());
        assertEquals("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", rs.getDescription());
        assertEquals(LocalDate.of(1967, 3, 25), rs.getReleaseDate());
        assertEquals(100, rs.getDuration());
    }

    //releaseDate = 27.12.1895
    @Test
    void postFilm_releaseDateEarlierThan28121895_returnException() {
        Film film = new Film();
        film.setName("Film name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(200);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.postFilm(film));

        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", exception.getMessage());
    }

    //releaseDate = 28.12.1895
    @Test
    void postFilm_releaseDateEquals28121895_returnException() {
        Film film = new Film();
        film.setName("Film name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(200);

        Film rs = filmController.postFilm(film);

        assertNotNull(rs.getId());
        assertEquals("Film name", rs.getName());
        assertEquals("Description", rs.getDescription());
        assertEquals(LocalDate.of(1895, 12, 28), rs.getReleaseDate());
        assertEquals(200, rs.getDuration());
    }

    //duration = 0
    @Test
    void postFilm_durationEquals0_returnException() {
        Film film = new Film();
        film.setName("Film name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1967, 3, 25));
        film.setDuration(0);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.postFilm(film));

        assertEquals("Продолжительность фильма должна != 0 и быть положительным числом", exception.getMessage());
    }

    //duration = -1
    @Test
    void postFilm_durationEqualsMinus1_returnException() {
        Film film = new Film();
        film.setName("Film name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1967, 3, 25));
        film.setDuration(-1);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.postFilm(film));

        assertEquals("Продолжительность фильма должна != 0 и быть положительным числом", exception.getMessage());
    }

    //duration = 1
    @Test
    void postFilm_durationEquals1_filmAdded() {
        Film film = new Film();
        film.setName("Film name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1967, 3, 25));
        film.setDuration(1);

        Film rs = filmController.postFilm(film);

        assertNotNull(rs.getId());
        assertEquals("Film name", rs.getName());
        assertEquals("Description", rs.getDescription());
        assertEquals(LocalDate.of(1967, 3, 25), rs.getReleaseDate());
        assertEquals(1, rs.getDuration());
    }

    /**
     * PUT /films
     */
    @Test
    void putFilm_nameBlank_returnException() {
        Film film = new Film();
        film.setName("Film name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1900, 3, 25));
        film.setDuration(200);

        Film rs = filmController.postFilm(film);
        Long id = rs.getId();

        Film anotherFilm = new Film();
        anotherFilm.setId(id);
        anotherFilm.setName("");
        anotherFilm.setDescription("Description another");
        anotherFilm.setReleaseDate(LocalDate.of(1901, 3, 25));
        anotherFilm.setDuration(20);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.putFilm(anotherFilm));

        assertEquals("Название фильма НЕ может быть пустым", exception.getMessage());
    }

    //description = 201 символ
    @Test
    void putFilm_descriptionLongerThan200_returnException() {
        Film film = new Film();
        film.setName("Film name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1900, 3, 25));
        film.setDuration(200);

        Film rs = filmController.postFilm(film);
        Long id = rs.getId();

        Film anotherFilm = new Film();
        anotherFilm.setId(id);
        anotherFilm.setName("Film name another");
        anotherFilm.setDescription("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901");
        anotherFilm.setReleaseDate(LocalDate.of(1901, 3, 25));
        anotherFilm.setDuration(20);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.putFilm(anotherFilm));

        assertEquals("Максимальная длина описания 200 символов", exception.getMessage());
    }

    //description = 200 символов
    @Test
    void putFilm_description200_filmAdded() {
        Film film = new Film();
        film.setName("Film name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1900, 3, 25));
        film.setDuration(200);

        Film rs = filmController.postFilm(film);
        Long id = rs.getId();

        Film anotherFilm = new Film();
        anotherFilm.setId(id);
        anotherFilm.setName("Film name another");
        anotherFilm.setDescription("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        anotherFilm.setReleaseDate(LocalDate.of(1901, 3, 25));
        anotherFilm.setDuration(20);

        Film anotherRs = filmController.putFilm(anotherFilm);

        assertNotNull(anotherRs.getId());
        assertEquals("Film name another", anotherRs.getName());
        assertEquals("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", anotherRs.getDescription());
        assertEquals(LocalDate.of(1901, 3, 25), anotherRs.getReleaseDate());
        assertEquals(20, anotherRs.getDuration());
    }

    //releaseDate = 27.12.1895
    @Test
    void putFilm_releaseDateEarlierThan28121895_returnException() {
        Film film = new Film();
        film.setName("Film name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1900, 3, 25));
        film.setDuration(200);

        Film rs = filmController.postFilm(film);
        Long id = rs.getId();

        Film anotherFilm = new Film();
        anotherFilm.setId(id);
        anotherFilm.setName("Film name another");
        anotherFilm.setDescription("Description another");
        anotherFilm.setReleaseDate(LocalDate.of(1895, 12, 27));
        anotherFilm.setDuration(20);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.putFilm(anotherFilm));

        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", exception.getMessage());
    }

    //releaseDate = 28.12.1895
    @Test
    void putFilm_releaseDateEquals28121895_returnException() {
        Film film = new Film();
        film.setName("Film name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1900, 3, 25));
        film.setDuration(200);

        Film rs = filmController.postFilm(film);
        Long id = rs.getId();

        Film anotherFilm = new Film();
        anotherFilm.setId(id);
        anotherFilm.setName("Film name another");
        anotherFilm.setDescription("Description another");
        anotherFilm.setReleaseDate(LocalDate.of(1895, 12, 28));
        anotherFilm.setDuration(20);

        Film anotherRs = filmController.putFilm(anotherFilm);

        assertNotNull(anotherRs.getId());
        assertEquals("Film name another", anotherRs.getName());
        assertEquals("Description another", anotherRs.getDescription());
        assertEquals(LocalDate.of(1895, 12, 28), anotherRs.getReleaseDate());
        assertEquals(20, anotherRs.getDuration());
    }

    //duration = 0
    @Test
    void putFilm_durationEquals0_returnException() {
        Film film = new Film();
        film.setName("Film name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1900, 3, 25));
        film.setDuration(200);

        Film rs = filmController.postFilm(film);
        Long id = rs.getId();

        Film anotherFilm = new Film();
        anotherFilm.setId(id);
        anotherFilm.setName("Film name another");
        anotherFilm.setDescription("Description another");
        anotherFilm.setReleaseDate(LocalDate.of(1967, 3, 25));
        anotherFilm.setDuration(0);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.putFilm(anotherFilm));

        assertEquals("Продолжительность фильма должна != 0 и быть положительным числом", exception.getMessage());
    }

    //duration = -1
    @Test
    void putFilm_durationEqualsMinus1_returnException() {
        Film film = new Film();
        film.setName("Film name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1900, 3, 25));
        film.setDuration(200);

        Film rs = filmController.postFilm(film);
        Long id = rs.getId();

        Film anotherFilm = new Film();
        anotherFilm.setId(id);
        anotherFilm.setName("Film name another");
        anotherFilm.setDescription("Description another");
        anotherFilm.setReleaseDate(LocalDate.of(1967, 3, 25));
        anotherFilm.setDuration(-1);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.putFilm(anotherFilm));

        assertEquals("Продолжительность фильма должна != 0 и быть положительным числом", exception.getMessage());
    }

    //duration = 1
    @Test
    void putFilm_durationEquals1_filmAdded() {
        Film film = new Film();
        film.setName("Film name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1900, 3, 25));
        film.setDuration(200);

        Film rs = filmController.postFilm(film);
        Long id = rs.getId();

        Film anotherFilm = new Film();
        anotherFilm.setId(id);
        anotherFilm.setName("Film name another");
        anotherFilm.setDescription("Description another");
        anotherFilm.setReleaseDate(LocalDate.of(1967, 3, 25));
        anotherFilm.setDuration(1);

        Film anotherRs = filmController.putFilm(anotherFilm);

        assertNotNull(anotherRs.getId());
        assertEquals("Film name another", anotherRs.getName());
        assertEquals("Description another", anotherRs.getDescription());
        assertEquals(LocalDate.of(1967, 3, 25), anotherRs.getReleaseDate());
        assertEquals(1, anotherRs.getDuration());
    }
}
