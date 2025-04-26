package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "requests")
@Data
@ToString
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "created_date")
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User requester;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "request_id")
    private Set<Item> items = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemRequest)) return false;
        return id != null && id.equals(((ItemRequest) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}