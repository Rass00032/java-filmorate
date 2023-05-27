package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFound;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

import static ru.yandex.practicum.filmorate.storage.mapper.MPAMapper.rowMapperMPA;


@Slf4j
@Component
@Qualifier("MPADbStorage")
public class MPADbStorage implements MPAStorage {

    private final JdbcTemplate jdbcTemplate;

    public MPADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MPA> getAllMPA() {
        List<MPA> mpaList = jdbcTemplate.query("SELECT * FROM mpa", rowMapperMPA());
        return mpaList;
    }

    @Override
    public MPA getMPA(int id) {
        int exists = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM mpa WHERE id = ?", Integer.class, id);
        if(exists == 0) throw new ObjectNotFound("Объект " +id +" не найден");
        return jdbcTemplate.queryForObject("SELECT * FROM mpa WHERE id = ?", rowMapperMPA(), id);
    }
}
