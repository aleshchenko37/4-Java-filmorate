package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.GenreAndMpaStorage;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
public class GenreAndMpaDbStorage implements GenreAndMpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreAndMpaDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public Collection<Genre> findAllGenres() {
        String sql = "select * from genre order by genre_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(rs.getInt("genre_id"), rs.getString("name")));
    }

    @Override
    public Optional<Genre> findGenreById(int id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from genre where genre_id = ?", id);
        if(genreRows.next()) {
            Genre genre = new Genre(genreRows.getInt("genre_id"),
                    genreRows.getString("name")
            );
            log.info("Найден жанр: {} {}", genre.getId(), genre.getName());
            return Optional.of(genre);
        } else {
            log.info("Жанр с идентификатором {} не найден.", id);
            throw new ObjectNotFoundException("Жанр с идентификатором " + id + " не найден.");
        }
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        String sql = "select * from rating order by rating_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Mpa(rs.getInt("rating_id"), rs.getString("name")));
    }

    @Override
    public Optional<Mpa> findMpaById(int id) {
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet("select * from rating where rating_id = ?", id);
        if(ratingRows.next()) {
            Mpa mpa = new Mpa(ratingRows.getInt("rating_id"),
                    ratingRows.getString("name")
            );
            log.info("Найден рейтинг: {} {}", mpa.getId(), mpa.getName());
            return Optional.of(mpa);
        } else {
            log.info("Рейтинг с идентификатором {} не найден.", id);
            throw new ObjectNotFoundException("Рейтинг с идентификатором " + id + " не найден.");
        }
    }
}
