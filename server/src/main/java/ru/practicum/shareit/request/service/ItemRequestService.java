package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequest createRequest(long userId, ItemRequestDto itemRequestDto);

    ItemRequest getItemRequestById(long userId, long requestId);

    Collection<ItemRequest> getItemRequestsByUserId(long userId);

    Collection<ItemRequest> getItemRequestsByOthers(long userId);
}
