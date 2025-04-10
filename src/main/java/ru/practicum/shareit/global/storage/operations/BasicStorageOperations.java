package ru.practicum.shareit.global.storage.operations;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.practicum.shareit.user.exception.InternalServerException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public abstract class BasicStorageOperations<T> {

    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;

    protected BasicStorageOperations(JdbcTemplate jdbc, RowMapper<T> mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    protected Optional<T> getSingle(String query, Object... params) {
        try {
            T result = jdbc.queryForObject(query, mapper, params);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    protected List<T> getMultiple(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }

    protected void delete(String query, long id) {
        jdbc.update(query, id);
    }

    protected void update(String query, Object... params) {
        if (jdbc.update(query, params) == 0) {
            throw new InternalServerException("Failed to update data.");
        }
    }

    protected long insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int updateCount = jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps;
        }, keyHolder);

        if (updateCount == 0) {
            throw new InternalServerException("Failed to insert data.");
        }

        Long id = keyHolder.getKeyAs(Long.class);

        if (id == null) {
            throw new InternalServerException("Failed to retrieve generated ID.");
        }
        return id;
    }

}