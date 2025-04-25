package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void addUser() {
        User user = new User();
        user.setName("name");
        user.setEmail("ddd@gmail.com");
        user = userRepository.save(user);
    }

    @Test
    void existsUserById() {

        Assertions.assertTrue(userRepository.existsUserById(2L));
    }

    @Test
    void findById() {
        Assertions.assertTrue(userRepository.findById(1L).isPresent());
    }

    @Test
    void findByEmail() {
        Assertions.assertTrue(userRepository.findByEmail("ddd@gmail.com").isPresent());
    }

    @AfterEach
    public void delUser() {
        userRepository.deleteAll();
    }
}