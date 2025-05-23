package ru.practicum.shareit.user.exception;

import lombok.Getter;
import ru.practicum.shareit.global.exception.BadRequestException;
import ru.practicum.shareit.user.model.User;

@Getter
public class UserCreationException extends BadRequestException {

  private final String message;
  private final String creationMessage;

  public UserCreationException(String message, String creationMessage) {
    this.message = message;
    this.creationMessage = creationMessage;
  }

  public Class<?> getEntityType() {
    return User.class;
  }
}