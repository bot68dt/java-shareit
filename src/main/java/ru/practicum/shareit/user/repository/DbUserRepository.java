package ru.practicum.shareit.user.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.global.storage.operations.BasicStorageOperations;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@Repository
@Primary
public class DbUserRepository extends BasicStorageOperations<User> implements UserStorage {

    private static final String CHECK_EXISTS_QUERY = """
        SELECT EXISTS
            (SELECT
                1
            FROM users
            WHERE user_id = ?)
        """;
    private static final String GET_BY_ID_QUERY = """
        SELECT
            *
        FROM users
        WHERE user_id = ?
        """;
    private static final String GET_BY_EMAIL_QUERY = """
        SELECT
            *
        FROM users
        WHERE email = ?
        """;
    private static final String INSERT_QUERY = """
        INSERT INTO users
            (email, name)
        VALUES (?, ?)
        """;
    private static final String UPDATE_QUERY = """
        UPDATE users
        SET
            email = ?,
            name = ?
        WHERE user_id = ?
        """;
    private static final String DELETE_QUERY = """
        DELETE
        FROM users
        WHERE user_id = ?
        """;

    public DbUserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public boolean checkUserExists(long userId) {
        return Boolean.TRUE.equals(jdbc.queryForObject(CHECK_EXISTS_QUERY, Boolean.class, userId));
    }

    @Override
    public Optional<User> getUserById(long userId) {
        return getSingle(GET_BY_ID_QUERY, userId);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return getSingle(GET_BY_EMAIL_QUERY, email);
    }

    @Override
    public long addUser(UserDto userDto) {
        return insert(INSERT_QUERY, userDto.getEmail(), userDto.getName());
    }

    @Override
    public void updateUser(User user) {
        update(UPDATE_QUERY, user.getEmail(), user.getName(), user.getId());
    }

    @Override
    public void deleteUser(long userId) {
        delete(DELETE_QUERY, userId);
    }
}
