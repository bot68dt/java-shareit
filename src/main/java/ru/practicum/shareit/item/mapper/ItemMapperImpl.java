package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapperImpl implements ItemMapper {

    @Override
    public ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
    }

    @Override
    public Item mapToItemModel(ItemDto newItemRequest) {
        Item item = new Item();
        item.setName(newItemRequest.getName());
        item.setDescription(newItemRequest.getDescription());
        item.setAvailable(newItemRequest.getAvailable());
        item.setOwner(null);
        item.setRequest(null);
        return item;
    }

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
