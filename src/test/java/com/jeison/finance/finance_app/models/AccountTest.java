package com.jeison.finance.finance_app.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class AccountTest {

    @Test
    void testEquals() {
        User user = new User();
        user.setId(1L);
        Account account1 = new Account(1L, "Efectivo", new BigDecimal(0), user);
        Account account2 = new Account(1L, "Efectivo", new BigDecimal(0), user);
        assertEquals(account1, account2);
    }
}
