package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public interface ItemMapper {

    ItemDto mapToItemDto(Item item);

    Item mapToItemModel(ItemDto newItemRequest);

    Item updateItemFields(Item item, ItemDto updateItemRequest);
}
