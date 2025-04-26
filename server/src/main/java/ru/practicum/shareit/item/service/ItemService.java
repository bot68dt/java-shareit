package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.Collection;

public interface ItemService {
    Collection<ItemTimingDto> getItemsByUserId(long userId, Integer from, Integer size);

    ItemTimingDto getItemById(long userId, long itemId);

    Collection<ItemDto> searchItemByText(long userId, String text, Integer from, Integer size);

    ItemDto createItem(long userId, ItemDto newItemRequest);

    ItemDto updateItem(long userId, ItemDto updateItemRequest, long itemId);

    CommentResponseDto comment(long userId, CommentDto commentDto, long itemId);
}
