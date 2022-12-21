package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.DBMpaStorage;

import java.util.List;

@Service
public class MpaService {
    DBMpaStorage dbMpaStorage;
    @Autowired
    public MpaService(DBMpaStorage dbMpaStorage) {
        this.dbMpaStorage = dbMpaStorage;
    }

    public List<Mpa> getAll(){
        return dbMpaStorage.getAll();
    }

    public Mpa getRatingById(Long id){
        return dbMpaStorage.getRatingBy(id);
    }
}
