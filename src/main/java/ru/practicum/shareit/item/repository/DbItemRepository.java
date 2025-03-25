package ru.practicum.shareit.item.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.global.storage.operations.BasicStorageOperations;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class DbItemRepository extends BasicStorageOperations<Item> implements ItemStorage {

    private static final String GET_ALL_ITEMS_BY_USERID = """
        SELECT
            i.item_id as item_id,
            i.name as item_name,
            i.description as description,
            i.available as available,
            u.user_id as user_id,
            u.email as email,
            u.name as user_name
        FROM items as i
        LEFT JOIN users AS u ON i.user_id = u.user_id
        WHERE u.user_id = ?
        GROUP BY
            i.item_id
        ORDER BY
            i.name
        """;
    private static final String GET_ITEM_BY_ITEMID_QUERY = """
        SELECT
            i.item_id as item_id,
            i.name as item_name,
            i.description as description,
            i.available as available,
            u.user_id as user_id,
            u.email as email,
            u.name as user_name
        FROM items as i
        LEFT JOIN users AS u ON i.user_id = u.user_id
        WHERE i.item_id = ?
        GROUP BY
            i.item_id
        ORDER BY
            i.name
        """;
    private static final String GET_ITEM_BY_TEXT_QUERY = """
        SELECT
            i.item_id as item_id,
            i.name as item_name,
            i.description as description,
            i.available as available,
            u.user_id as user_id,
            u.email as email,
            u.name as user_name
        FROM items as i
        LEFT JOIN users AS u ON i.user_id = u.user_id
        WHERE (((UPPER (i.name) LIKE ?) OR (UPPER (i.description) LIKE ?)) AND i.available = true)
        GROUP BY
            i.item_id
        ORDER BY
            i.name
        """;
    private static final String INSERT_QUERY = """
        INSERT INTO items
            (name, description, available, user_id)
        VALUES (?, ?, ?, ?)
        """;
    private static final String UPDATE_QUERY = """
        UPDATE items
        SET
            name = ?,
            description = ?,
            available = ?,
            user_id = ?,
            request_id =?
        WHERE user_id = ?
        """;

    public DbItemRepository(JdbcTemplate jdbc, RowMapper<Item> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Item> getItemsByUserId(long userId) {
        return getMultiple(GET_ALL_ITEMS_BY_USERID, userId);
    }

    @Override
    public Optional<Item> getItemById(long itemId) {
        return getSingle(GET_ITEM_BY_ITEMID_QUERY, itemId);
    }

    @Override
    public List<Item> getItemByText(String text) {
        return getMultiple(GET_ITEM_BY_TEXT_QUERY, text.toUpperCase(), text.toUpperCase());
    }

    @Override
    public long createItem(ItemDto itemDto, long userId) {
        return insert(INSERT_QUERY, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), userId);
    }

    @Override
    public void updateItem(Item item) {
        update(UPDATE_QUERY, item.getName(), item.getDescription(), item.getAvailable(), item.getOwner().getId(), null, item.getOwner().getId());
    }
}