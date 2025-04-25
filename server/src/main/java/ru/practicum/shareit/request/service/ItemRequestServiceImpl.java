package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestsMapperNew;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequest createRequest(long userId, ItemRequestDto itemRequestDto) {

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.warn("Adding ItemRequest failed: user with ID {} not found", userId);
            throw new UserNotFoundException("Error when creating ItemRequest. User not found", userId);
        }

        ItemRequest itemRequest = ItemRequestsMapperNew.mapToNewItemRequest(itemRequestDto, user.get());
        log.debug("Adding new itemRequest {}", itemRequest);
        itemRequest = itemRequestRepository.saveAndFlush(itemRequest);
        return itemRequest;
    }

    @Override
    public ItemRequest getItemRequestById(long userId, long requestId) {

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.warn("Getting ItemRequest failed: user with ID {} not found", userId);
            throw new UserNotFoundException("Error when getting ItemRequest. User not found", userId);
        }

        Optional<ItemRequest> itemRequest = itemRequestRepository.findById(requestId);
        if (itemRequest.isEmpty()) {
            log.warn("Getting ItemRequest failed: ItemRequest with ID {} not found", userId);
            throw new UserNotFoundException("Error when getting ItemRequest. ItemRequest not found", userId);
        }
        log.debug("Returning itemRequest {}", itemRequest);
        return itemRequest.get();
    }

    @Override
    public Collection<ItemRequest> getItemRequestsByUserId(long userId) {

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.warn("Getting ItemRequest failed: user with ID {} not found", userId);
            throw new UserNotFoundException("Error when getting ItemRequest. User not found", userId);
        }

        List<ItemRequest> itemRequests = itemRequestRepository.findByRequesterIdOrderByCreatedDesc(userId);
        log.debug("Returning itemRequests {}", itemRequests);
        return itemRequests;
    }

    @Override
    public  Collection<ItemRequest> getItemRequestsByOthers(long userId) {

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.warn("Getting ItemRequest failed: user with ID {} not found", userId);
            throw new UserNotFoundException("Error when getting ItemRequest. User not found", userId);
        }

        List<ItemRequest> itemRequests = itemRequestRepository.findByRequesterIdNotOrderByCreatedDesc(userId);
        log.debug("Returning itemRequests {}", itemRequests);
        return itemRequests;
    }
}
