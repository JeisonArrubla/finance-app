package com.jeison.finance.finance_app.services;

import com.jeison.finance.finance_app.models.Transaction;

public interface TransactionService {

    Transaction create(Transaction transaction, String username);
}
