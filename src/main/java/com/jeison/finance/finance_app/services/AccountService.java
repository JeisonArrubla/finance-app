package com.jeison.finance.finance_app.services;

import java.math.BigDecimal;
// import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.jeison.finance.finance_app.dto.AccountDto;
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

    public List<AccountDto> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId).stream()
                .map(a -> new AccountDto(a.getDescription(), a.getBalance()))
                .collect(Collectors.toList());

    }

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
        if (!userRepository.existsById(account.getUser().getId()))
            throw new ResourceNotFoundException("Usuario no encontrado");
        if (accountRepository.findByDescriptionAndUser(account.getDescription(), account.getUser()).isPresent())
            throw new DuplicateKeyException("Nombre de cuenta ya existe");
        return accountRepository.save(account);
    }

    public Map<String, String> deleteAccount(Account account) {
        if (account == null)
            throw new NullPointerException("La cuenta no puede ser nula");
        if (account.getId() == null)
            throw new NullFieldException("El id no puede ser nulo");
        if (!accountRepository.existsById(account.getId()))
            throw new ResourceNotFoundException("Cuenta no encontrada");
        accountRepository.delete(account);
        return Collections.singletonMap("message", "Cuenta eliminada con éxito");
    }
}
