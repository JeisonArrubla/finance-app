package com.jeison.finance.finance_app.services;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeison.finance.finance_app.exceptions.NullFieldException;
import com.jeison.finance.finance_app.exceptions.ResourceNotFoundException;
import com.jeison.finance.finance_app.models.Account;
import com.jeison.finance.finance_app.repositories.AccountRepository;
import com.jeison.finance.finance_app.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Account> getAccountsByUserId(Long userId) {
        return repository.findByUserId(userId);

    }

    @Transactional
    public Map<String, String> createAccount(Account account) {
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
        if (repository.findByDescriptionAndUser(account.getDescription(), account.getUser()).isPresent())
            throw new DuplicateKeyException("Nombre de cuenta ya existe");
        repository.save(account);
        return Collections.singletonMap("message", "Cuenta creada con éxito");
    }

    @Transactional
    public Optional<Account> update(Long id, Account account) {
        Optional<Account> accountOptional = repository.findById(id);
        if (accountOptional.isPresent()) {
            Account accountDb = accountOptional.orElseThrow();
            accountDb.setDescription(account.getDescription());
            return Optional.of(repository.save(accountDb));
        }
        return accountOptional;
    }

    @Transactional
    public Map<String, String> delete(Long id) {
        Optional<Account> accountOptional = repository.findById(id);
        if (accountOptional.isPresent()) {
            repository.delete(accountOptional.orElseThrow());
            return Collections.singletonMap("message", "Cuenta eliminada con éxito");
        }
        throw new EntityNotFoundException();
    }
}
