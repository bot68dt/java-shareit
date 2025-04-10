package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    private long id;
    @Email
    private String email;
    private String name;
}