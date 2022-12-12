package ru.yandex.practicum.filmorate.storage.films;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Long id = 1L;
    private HashMap<Long, Film> films = new HashMap<>();

    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    public Film create(Film film) {
            film.setId(id);
            id++;
            film.setLikes(new HashSet<>());
            films.put(film.getId(), film);
            return film;
    }

    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            film.setLikes(new HashSet<>());
            films.put(film.getId(), film);
            return film;
        }
        throw new ObjectNotFoundException("Фильм не найден");
    }

    public Film getFilmByID(Long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        }
        throw new ObjectNotFoundException("Фильм не найден");
    }

    public void like(Long id, Long userID) {
        getFilmByID(id).like(userID);
    }

    public void removeLike(Long id, Long userID) {
        getFilmByID(id).removeLike(userID);
    }

    public List<Film> getTheBest(Integer count) {
        List<Film> sortedList = new ArrayList<>(getAll());
        Collections.reverse(sortedList);
        return sortedList.stream().limit(count).collect(Collectors.toList());
    }
}
