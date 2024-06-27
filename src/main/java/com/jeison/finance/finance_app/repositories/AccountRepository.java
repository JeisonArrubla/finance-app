package com.jeison.finance.finance_app.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.jeison.finance.finance_app.models.Account;
import com.jeison.finance.finance_app.models.User;

public interface AccountRepository extends CrudRepository<Account, Long> {

    Optional<Account> findByDescriptionAndUser(String description, User user);
}
