package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.DbGenreStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
public class GenreService {
    private final GenreStorage genreStorage;
    @Autowired
    public GenreService(DbGenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genre> getAll(){
        return genreStorage.findAll();
    }

    public Genre getGenreById(Long id){
        return genreStorage.findGenreById(id).orElseThrow(() -> new ObjectNotFoundException("Жанр не найден"));
    }
}
