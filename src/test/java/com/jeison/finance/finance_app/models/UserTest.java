package com.jeison.finance.finance_app.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    void testEquals() {
        User user1 = new User(1L, "Pepe", "12345", true);
        User user2 = new User(1L, "Pepe", "12345", true);
        assertEquals(user1, user2);
    }
}
