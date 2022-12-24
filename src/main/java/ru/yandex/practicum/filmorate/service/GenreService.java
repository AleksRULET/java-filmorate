package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.DbGenreStorage;

import java.util.List;

@Service
public class GenreService {
    private final DbGenreStorage DbGenreStorage;
    @Autowired
    public GenreService(DbGenreStorage DbGenreStorage) {
        this.DbGenreStorage = DbGenreStorage;
    }

    public List<Genre> getAll(){
        return DbGenreStorage.getAll();
    }

    public Genre getGenreById(Long id){
        return DbGenreStorage.getGenreById(id);
    }
}
