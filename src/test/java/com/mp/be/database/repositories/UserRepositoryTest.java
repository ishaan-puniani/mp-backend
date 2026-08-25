package com.mp.be.database.repositories;

import com.mp.be.database.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        user = new User();
        user.setEmail("test" + System.currentTimeMillis() + "@example.com");
        user.setPassword("password");
        userRepository.save(user);
    }

    @Test
    void testFindById() {
        Optional<User> foundUser = userRepository.findById(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void testFindByEmail() {
        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());
        assertTrue(foundUser.isPresent());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void testFindByPasswordResetToken() {
        user.setPasswordResetToken("resetToken");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByPasswordResetToken("resetToken");
        assertTrue(foundUser.isPresent());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void testFindByEmailVerificationToken() {
        user.setEmailVerificationToken("verificationToken");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmailVerificationToken("verificationToken");
        assertTrue(foundUser.isPresent());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void testDeleteById() {
        userRepository.deleteById(user.getId());
        Optional<User> foundUser = userRepository.findById(user.getId());
        assertFalse(foundUser.isPresent());
    }
} 