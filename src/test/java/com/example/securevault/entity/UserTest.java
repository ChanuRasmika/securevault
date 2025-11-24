package com.example.securevault.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void user_ShouldCreateWithAllFields() {
        String sub = "user123";
        String email = "test@example.com";

        User user = new User();
        user.setSub(sub);
        user.setEmail(email);

        assertEquals(sub, user.getSub());
        assertEquals(email, user.getEmail());
    }

    @Test
    void user_ShouldSupportAllArgsConstructor() {
        String sub = "user123";
        String email = "test@example.com";

        User user = new User(sub, email);

        assertEquals(sub, user.getSub());
        assertEquals(email, user.getEmail());
    }

    @Test
    void user_ShouldSupportNoArgsConstructor() {
        User user = new User();
        
        assertNull(user.getSub());
        assertNull(user.getEmail());
    }

    @Test
    void user_ShouldHandleNullValues() {
        User user = new User();
        user.setSub(null);
        user.setEmail(null);

        assertNull(user.getSub());
        assertNull(user.getEmail());
    }
}