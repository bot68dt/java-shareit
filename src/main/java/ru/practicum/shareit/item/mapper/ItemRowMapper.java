package ru.practicum.shareit.item.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ItemRowMapper implements RowMapper<Item> {

    @Override
    public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
        Item item = new Item();
        User user = new User();
        item.setId(rs.getLong("item_id"));
        item.setName(rs.getString("item_name"));
        item.setDescription(rs.getString("description"));
        item.setAvailable(rs.getBoolean("available"));
        user.setId(rs.getLong("user_id"));
        user.setEmail(rs.getString("email"));
        user.setName(rs.getString("user_name"));
        item.setOwner(user);
        item.setRequest(null);
        return item;
    }
}