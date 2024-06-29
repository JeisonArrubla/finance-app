package com.jeison.finance.finance_app.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.jeison.finance.finance_app.exceptions.NullFieldException;
import com.jeison.finance.finance_app.exceptions.ResourceNotFoundException;
import com.jeison.finance.finance_app.models.Account;
import com.jeison.finance.finance_app.repositories.AccountRepository;
import com.jeison.finance.finance_app.repositories.UserRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    public Account saveAccount(Account account) {
        if (account == null)
            throw new NullPointerException("La cuenta no puede ser nula");
        if (account.getDescription() == null)
            throw new NullFieldException("La descripción de la cuenta no puede ser nula");
        if (account.getUser() == null)
            throw new NullFieldException("El usuario no puede ser nulo");
        if (account.getUser().getId() == null)
            throw new NullFieldException("El id del usuario no puede ser nulo");
        if (account.getBalance() == null)
            account.setBalance(BigDecimal.ZERO);
        if (userRepository.findById(account.getUser().getId()).isEmpty())
            throw new ResourceNotFoundException("Usuario no encontrado");
        if (accountRepository.findByDescriptionAndUser(account.getDescription(), account.getUser()).isPresent())
            throw new DuplicateKeyException("Nombre de cuenta ya existe");
        return accountRepository.save(account);
    }
}
