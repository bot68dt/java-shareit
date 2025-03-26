package ru.practicum.shareit.item.exception;

import lombok.Getter;
import ru.practicum.shareit.item.model.Item;

@Getter
public class ItemCreationException extends RuntimeException {

  private final String message;
  private final String creationMessage;

  public ItemCreationException(String message, String creationMessage) {
    this.message = message;
    this.creationMessage = creationMessage;
  }

  public Class<?> getEntityType() {
    return Item.class;
  }
}