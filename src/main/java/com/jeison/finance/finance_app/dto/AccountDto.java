package com.jeison.finance.finance_app.dto;

import java.math.BigDecimal;

public class AccountDto {

    private String description;
    private BigDecimal balance;

    public AccountDto(String description, BigDecimal balance) {
        this.description = description;
        this.balance = balance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
