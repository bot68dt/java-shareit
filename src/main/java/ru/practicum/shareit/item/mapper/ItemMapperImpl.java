package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapperImpl implements ItemMapper {

    @Override
    public Item updateItemFields(Item item, ItemDto updateItemRequest) {
        if (updateItemRequest.getName() != null) {
            item.setName(updateItemRequest.getName());
        }
        if (updateItemRequest.getDescription() != null) {
            item.setDescription(updateItemRequest.getDescription());
        }
        if (updateItemRequest.getAvailable() != null) {
            item.setAvailable(updateItemRequest.getAvailable());
        }
        return item;
    }
}
