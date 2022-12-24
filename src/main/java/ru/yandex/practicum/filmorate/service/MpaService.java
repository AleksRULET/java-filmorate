package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.DbMpaStorage;

import java.util.List;

@Service
public class MpaService {
    private final DbMpaStorage DbMpaStorage;
    @Autowired
    public MpaService(DbMpaStorage DbMpaStorage) {
        this.DbMpaStorage = DbMpaStorage;
    }

    public List<Mpa> getAll(){
        return DbMpaStorage.getAll();
    }

    public Mpa getRatingById(Long id){
        return DbMpaStorage.getRatingBy(id);
    }
}
