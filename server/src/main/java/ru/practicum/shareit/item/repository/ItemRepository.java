package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwnerId(long userId, Pageable pageable);

    Optional<Item> findDistinctById(long itemId);

    @Query(" select i from Item i where (upper(i.name) like ?1 or upper(i.description) like ?1)  and i.available = true ")
    List<Item> getItemByText(String text, Pageable pageable);
}