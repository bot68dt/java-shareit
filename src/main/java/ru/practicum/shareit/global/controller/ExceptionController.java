package ru.practicum.shareit.global.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.exception.ItemCreationException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemValidationException;
import ru.practicum.shareit.global.dto.ErrorMessage;
import ru.practicum.shareit.user.exception.*;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorMessage> handleInternalServerException(final RuntimeException e) {
        log.warn("Encountered {}: returning 500 Internal Server Error. Message: {}", e.getClass().getSimpleName(), e.getMessage());
        ErrorMessage errorMessage = new ErrorMessage("Internal server error.", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(errorMessage);
    }

    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<ErrorMessage> handleUserCreationException(final UserCreationException e) {
        log.warn("Encountered {}: returning 400 Bad Request. Message: {}", e.getClass().getSimpleName(), e.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), e.getCreationMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(errorMessage);
    }

    @ExceptionHandler(ItemCreationException.class)
    public ResponseEntity<ErrorMessage> handleItemCreationException(final ItemCreationException e) {
        log.warn("Encountered {}: returning 400 Bad Request. Message: {}", e.getClass().getSimpleName(), e.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), e.getCreationMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(errorMessage);
    }

    @ExceptionHandler(UserEmailException.class)
    public ResponseEntity<ErrorMessage> handleUserEmailException(final UserEmailException e) {
        log.warn("Encountered {}: returning 409 conflict. Message: {}", e.getClass().getSimpleName(), e.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), e.getCreationMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON).body(errorMessage);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUserNotFoundException(final UserNotFoundException e) {
        log.warn("Encountered {}: returning 404 Not Found. Message: {}", e.getClass().getSimpleName(), e.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), String.format("%s with ID %d not found", e.getEntityType().getSimpleName(), e.getId()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(errorMessage);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleItemNotFoundException(final ItemNotFoundException e) {
        log.warn("Encountered {}: returning 404 Not Found. Message: {}", e.getClass().getSimpleName(), e.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), String.format("%s with ID %d not found", e.getEntityType().getSimpleName(), e.getId()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(errorMessage);
    }

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<ErrorMessage> handleUserValidationException(final UserValidationException e) {
        log.warn("Encountered {}: returning 400 Bad Request. Message: {}", e.getClass().getSimpleName(), e.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), String.format("Invalid %s data: %s", e.getEntityType().getSimpleName(), e.getValidationMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(errorMessage);
    }

    @ExceptionHandler(ItemValidationException.class)
    public ResponseEntity<ErrorMessage> handleItemValidationException(final ItemValidationException e) {
        log.warn("Encountered {}: returning 400 Bad Request. Message: {}", e.getClass().getSimpleName(), e.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), String.format("Invalid %s data: %s", e.getEntityType().getSimpleName(), e.getValidationMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(errorMessage);
    }
}