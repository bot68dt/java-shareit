package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    List<Item> getItemsByUserId(long userId);

    Optional<Item> getItemById(long itemId);

    List<Item> getItemByText(String text);

    long createItem(ItemDto itemDto, long userId);

    void updateItem(Item item);
}
