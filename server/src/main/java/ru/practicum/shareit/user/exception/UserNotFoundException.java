package ru.practicum.shareit.user.exception;

import lombok.Getter;
import ru.practicum.shareit.global.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

@Getter
public class UserNotFoundException extends NotFoundException {

  private final String message;
  private final long id;

  public UserNotFoundException(String message, long id) {
    this.message = message;
    this.id = id;
  }

  public Class<?> getEntityType() {
    return User.class;
  }
}