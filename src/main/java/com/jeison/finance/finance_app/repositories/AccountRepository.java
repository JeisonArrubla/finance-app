package com.jeison.finance.finance_app.repositories;

import org.springframework.data.repository.CrudRepository;

import com.jeison.finance.finance_app.models.Account;

public interface AccountRepository extends CrudRepository<Account, Long> {

}
