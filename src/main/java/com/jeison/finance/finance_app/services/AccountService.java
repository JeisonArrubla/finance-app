package com.jeison.finance.finance_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jeison.finance.finance_app.models.Account;
import com.jeison.finance.finance_app.repositories.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;

    public Account saveAccount(Account account) {
        Account a = new Account();
        a.setDescription(account.getDescription());
        a.setBalance(account.getBalance());
        a.setUser(account.getUser());
        return repository.save(a);
    }
}
