package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestsMapperNew {

    public static ItemRequest mapToNewItemRequest(ItemRequestDto itemRequestDto, User user) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequester(user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setDescription(itemRequestDto.getDescription());
        return itemRequest;
    }
}
