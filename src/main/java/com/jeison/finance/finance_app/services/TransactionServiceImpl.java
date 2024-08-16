package com.jeison.finance.finance_app.services;

import com.jeison.finance.finance_app.models.Account;
import com.jeison.finance.finance_app.models.Transaction;
import com.jeison.finance.finance_app.repositories.AccountRepository;
import com.jeison.finance.finance_app.repositories.TransactionRepository;
import com.jeison.finance.finance_app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.jeison.finance.finance_app.models.TransactionType.*;
import static com.jeison.finance.finance_app.util.CommonUtils.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public Transaction create(Transaction transaction) {
        
        Account sourceAccount = accountRepository.findById(transaction.getSourceAccount().getId()).orElseThrow(() -> new IllegalArgumentException("Cuenta origen no encontrada"));
        Account destinationAccount;

        ensureAccountOwnership(sourceAccount);

        sourceAccount.setBalance(getSourceAccountNewBalance(transaction, sourceAccount));
        accountRepository.save(sourceAccount);

        if (transaction.getType().equals(TRANSFER)) {
            if (transaction.getDestinationAccount() == null)
                throw new IllegalArgumentException("Debes indicar la cuenta destino");

            destinationAccount = accountRepository.findById(transaction.getDestinationAccount().getId()).orElseThrow(() -> new IllegalArgumentException("Cuenta destino no encontrada"));

            if (sourceAccount.getId().compareTo(destinationAccount.getId()) == 0)
                throw new IllegalArgumentException("Cuenta origen y destino no pueden ser iguales");

            ensureAccountOwnership(destinationAccount);

            BigDecimal destinationAccountInitialBalance = destinationAccount.getBalance();
            BigDecimal destinationAccountNewBalance = destinationAccountInitialBalance.add(transaction.getAmount());
            destinationAccount.setBalance(destinationAccountNewBalance);

            accountRepository.save(destinationAccount);
        }

        if (transaction.getDate() == null)
            transaction.setDate(LocalDateTime.now());

        return repository.save(transaction);
    }

    private static BigDecimal getSourceAccountNewBalance(Transaction transaction, Account sourceAccount) {
        
        BigDecimal sourceAccountInitialBalance = sourceAccount.getBalance();
        BigDecimal sourceAccountNewBalance;

        if (transaction.getType().equals(WITHDRAWAL) || transaction.getType().equals(TRANSFER)) {
            sourceAccountNewBalance = sourceAccountInitialBalance.subtract(transaction.getAmount());
        } else if (transaction.getType().equals(DEPOSIT)) {
            sourceAccountNewBalance = sourceAccountInitialBalance.add(transaction.getAmount());
        } else {
            throw new IllegalArgumentException("El tipo de transacción no es válido");
        }

        return sourceAccountNewBalance;
    }

    private void ensureAccountOwnership(Account account) {

        if (userRepository.findByUsername(getCurrentUsername()).orElseThrow().getId()
                .compareTo(account.getUser().getId()) != 0)
            throw new AccessDeniedException("Transacción no autorizada");
    }
}
