package com.jeison.finance.finance_app.services;

import java.util.List;
import java.util.Optional;

import com.jeison.finance.finance_app.models.Account;

public interface AccountService {

    Account create(Account account, String username);

    Optional<Account> findById(Long id, String username);

    List<Account> findByUserId(Long userId, String username);

    Account update(Long id, Account account, String username);

    void delete(Long id, String username);
}
