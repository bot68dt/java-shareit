package ru.practicum.shareit.item.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exception.OwnerException;
import ru.practicum.shareit.booking.mapper.BookingMapperNew;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.exception.ItemCreationException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemValidationException;
import ru.practicum.shareit.item.mapper.CommentMapperNew;
import ru.practicum.shareit.item.mapper.ItemMapperNew;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final Validator validator;

    @Override
    public Collection<ItemTimingDto> getItemsByUserId(long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.warn("Getting user failed: user with ID {} not found", userId);
            throw new UserNotFoundException("Error when getting user", userId);
        }
        log.debug("Getting all items of the user with ID {}", userId);
        List<ItemTimingDto> itemTimingDTO = new ArrayList<>();
        List<ItemDto> itemDto = ItemMapperNew.mapToItemDto(itemRepository.findByOwnerId(userId));
        Optional<Booking> booking;
        BookingDto string1 = new BookingDto();
        BookingDto string2 = new BookingDto();
        List<Comment> comments;
        for (ItemDto itemDto1 : itemDto) {
            booking = bookingRepository.findFirst1ByItemIdAndItemOwnerIdAndStartBeforeOrderByStartDesc(itemDto1.getId(), userId, LocalDateTime.now());
            if (booking.isPresent()) string1 = BookingMapperNew.mapToBookingDto(booking.get());
            booking = bookingRepository.findFirst1ByItemIdAndItemOwnerIdAndStartAfterOrderByStart(itemDto1.getId(), userId, LocalDateTime.now());
            if (booking.isPresent()) string2 = BookingMapperNew.mapToBookingDto(booking.get());
            comments = commentRepository.findByItemId(itemDto1.getId());
            itemTimingDTO.add(new ItemTimingDto(itemDto1.getId(), itemDto1.getName(), itemDto1.getDescription(), itemDto1.getAvailable(), string1, string2, CommentMapperNew.mapToCommentResponseDto(comments)));
        }
        return itemTimingDTO;
    }

    @Override
    public ItemTimingDto getItemById(long userId, long itemId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.warn("Getting user failed: user with ID {} not found", userId);
            throw new UserNotFoundException("Error when getting user", userId);
        }
        log.debug("Getting item with ID {}", itemId);
        Optional<Item> item = itemRepository.findDistinctById(itemId);
        if (item.isEmpty()) {
            log.warn("Getting item failed: item with ID {} not found", itemId);
            throw new ItemNotFoundException("Error when getting item", itemId);
        }
        ItemTimingDto itemTimingDto = ItemMapperNew.mapToItemTimingDto(item.get());
        itemTimingDto.setComments(CommentMapperNew.mapToCommentResponseDto(commentRepository.findByItemId(itemId)));
        Optional<Booking> booking = bookingRepository.findFirst1ByItemIdAndItemOwnerIdAndStartBeforeOrderByStartDesc(itemId, userId, LocalDateTime.now());
        booking.ifPresent(value -> itemTimingDto.setLastBooking(BookingMapperNew.mapToBookingDto(value)));
        booking = bookingRepository.findFirst1ByItemIdAndItemOwnerIdAndStartAfterOrderByStart(itemId, userId, LocalDateTime.now());
        booking.ifPresent(value -> itemTimingDto.setNextBooking(BookingMapperNew.mapToBookingDto(value)));
        return itemTimingDto;
    }

    @Override
    public Collection<ItemDto> searchItemByText(long userId, String text) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.warn("Getting user failed: user with ID {} not found", userId);
            throw new UserNotFoundException("Error when getting user", userId);
        }
        log.debug("Getting item with text:  {}", text);
        return ItemMapperNew.mapToItemDto(itemRepository.getItemByText(text));
    }

    @Override
    public ItemDto createItem(long userId, ItemDto newItemRequest) {
        if (!userRepository.existsUserById(userId)) {
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
        Item item = ItemMapperNew.mapToNewItem(newItemRequest);
        User user = userRepository.findById(userId).get();
        item.setOwner(user);
        item = itemRepository.saveAndFlush(item);
        log.debug("Adding new item {}", newItemRequest);
        return ItemMapperNew.mapToItemDto(item);
    }

    @Override
    public ItemDto updateItem(long userId, ItemDto updateItemRequest, long itemId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Getting user failed: user with ID {} not found", userId);
            return new UserNotFoundException("Error when getting user", userId);
        });
        Optional<Item> item = itemRepository.findDistinctById(itemId);
        Item item1 = item.get();
        if (item.isPresent()) {
            if (!(item.get().getOwner().getId() == userId)) {
                log.warn("Updating failed: user with ID {} is not an owner of the item", userId);
                throw new UserNotFoundException("Updating failed", userId);
            }
            log.debug("Updating item with ID {}: {}", itemId, updateItemRequest);
            item1 = ItemMapperNew.updateItemFields(item.get(), updateItemRequest);
            item1 = itemRepository.saveAndFlush(item1);
        }
        return ItemMapperNew.mapToItemDto(item1);
    }

    @Override
    public CommentResponseDto comment(long userId, CommentDto commentDto, long itemId) {
        Optional<Booking> booking = bookingRepository.findFirst1ByItemIdAndBookerIdAndEndBefore(itemId, userId, LocalDateTime.now());
        if (booking.isEmpty()) {
            log.warn("Commenting failed: user with ID {} is not a booker of the item", userId);
            throw new OwnerException("Commenting failed. User with ID is not a booker of the item", userId);
        }
        Comment comment = new Comment();
        comment.setItem(booking.get().getItem());
        comment.setText(commentDto.getText());
        comment.setAuthor(booking.get().getBooker());
        comment.setCreated(LocalDateTime.now());
        comment = commentRepository.saveAndFlush(comment);
        return CommentMapperNew.mapToCommentResponseDto(comment);
    }
}