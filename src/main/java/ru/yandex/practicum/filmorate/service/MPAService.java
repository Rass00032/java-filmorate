package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.util.List;

@Service
public class MPAService {
    @Qualifier("MPADbStorage")
    private final MPAStorage storage;

    public MPAService(MPAStorage storage) {
        this.storage = storage;
    }

    public List<MPA> getAllMPA() {
        return storage.getAllMPA();
    }

    public MPA getMPA(int id) {
        return storage.getMPA(id);
    }
}
