package com.jeison.finance.finance_app.services;

import java.util.List;
import java.util.Optional;

import com.jeison.finance.finance_app.models.Account;

public interface AccountService {

    Account create(Account account);

    Optional<Account> findById(Long id);

    List<Account> findByUserId(Long userId);

    Account update(Long id, Account account);

    void delete(Long id);
}
