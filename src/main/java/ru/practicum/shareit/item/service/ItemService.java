package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    Collection<Item> getItemsByUserId(long userId);

    Item getItemById(long userId, long itemId);

    Collection<Item> searchItemByText(long userId, String text);

    Item createItem(long userId, ItemDto newItemRequest);

    Item updateItem(long userId, ItemDto updateItemRequest, long itemId);
}
