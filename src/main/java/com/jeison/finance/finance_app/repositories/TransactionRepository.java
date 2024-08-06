package com.jeison.finance.finance_app.repositories;

import com.jeison.finance.finance_app.models.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
