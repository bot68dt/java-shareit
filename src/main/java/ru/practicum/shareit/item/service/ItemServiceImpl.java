package ru.practicum.shareit.item.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemCreationException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemValidationException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final UserStorage userStorage;
    private final ItemStorage itemStorage;
    private final Validator validator;
    private final ItemMapper mapper;

    @Override
    public Collection<Item> getItemsByUserId(long userId) {
        Optional<User> user = userStorage.getUserById(userId);
        if (user.isEmpty()) {
            log.warn("Getting user failed: user with ID {} not found", userId);
            throw new UserNotFoundException("Error when getting user", userId);
        }
        log.debug("Getting all items of the user with ID {}", userId);
        return itemStorage.getItemsByUserId(userId);
    }

    @Override
    public Item getItemById(long userId, long itemId) {
        Optional<User> user = userStorage.getUserById(userId);
        if (user.isEmpty()) {
            log.warn("Getting user failed: user with ID {} not found", userId);
            throw new UserNotFoundException("Error when getting user", userId);
        }
        log.debug("Getting item with ID {}", itemId);
        Optional<Item> item = itemStorage.getItemById(itemId);
        if (item.isEmpty()) {
            log.warn("Getting item failed: item with ID {} not found", itemId);
            throw new ItemNotFoundException("Error when getting item", itemId);
        }
        return item.get();
    }

    @Override
    public Collection<Item> searchItemByText(long userId, String text) {
        Optional<User> user = userStorage.getUserById(userId);
        if (user.isEmpty()) {
            log.warn("Getting user failed: user with ID {} not found", userId);
            throw new UserNotFoundException("Error when getting user", userId);
        }
        log.debug("Getting item with text:  {}", text);
        return itemStorage.getItemByText(text);
    }

    @Override
    public Item createItem(long userId, ItemDto newItemRequest) {
        if (!userStorage.checkUserExists(userId)) {
            log.warn("Creating item failed: user with ID {} not found", userId);
            throw new UserNotFoundException("Error when creating item. User not found", userId);
        }
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(newItemRequest);
        if (!violations.isEmpty()) {
            log.warn("Adding item failed: {}", violations.iterator().next().getMessage());
            throw new ItemValidationException("Error when creating new item", violations.iterator().next().getMessage());
        }
        if (newItemRequest.getName() == null) {
            log.debug("Item had no name");
            throw new ItemCreationException("Error when creating new item", "Item has no name");
        }
        if (newItemRequest.getDescription() == null) {
            log.debug("User had no description");
            throw new ItemCreationException("Error when creating new item", "Item has no description");
        }
        if (newItemRequest.getAvailable() == null) {
            log.debug("User had no status");
            throw new ItemCreationException("Error when creating new item", "Item has no status");
        }
        long itemId = itemStorage.createItem(newItemRequest, userId);
        log.debug("Adding new item {}", newItemRequest);
        return getItemById(userId, itemId);
    }

    @Override
    public Item updateItem(long userId, ItemDto updateItemRequest, long itemId) {
        User user = userStorage.getUserById(userId).orElseThrow(() -> {
            log.warn("Getting user failed: user with ID {} not found", userId);
            return new UserNotFoundException("Error when getting user", userId);
        });
        Item item = getItemById(userId, itemId);
        if (!(item.getOwner().getId() == userId)) {
            log.warn("Updating failed: user with ID {} is not an owner of the item", userId);
            throw new UserNotFoundException("Updating failed", userId);
        }
        log.debug("Updating item with ID {}: {}", itemId, updateItemRequest);
        item = mapper.updateItemFields(item, updateItemRequest);
        itemStorage.updateItem(item);
        return item;
    }

}