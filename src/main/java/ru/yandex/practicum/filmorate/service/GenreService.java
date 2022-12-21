package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.DBGenreStorage;

import java.util.List;

@Service
public class GenreService {
    DBGenreStorage dbGenreStorage;
    @Autowired
    public GenreService(DBGenreStorage dbGenreStorage) {
        this.dbGenreStorage = dbGenreStorage;
    }

    public List<Genre> getAll(){
        return dbGenreStorage.getAll();
    }

    public Genre getGenreById(Long id){
        return dbGenreStorage.getGenreById(id);
    }
}
