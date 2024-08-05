package com.jeison.finance.finance_app.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeison.finance.finance_app.models.Account;
import com.jeison.finance.finance_app.repositories.AccountRepository;
import com.jeison.finance.finance_app.repositories.UserRepository;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public Account create(Account account, String username) {

        if (getCurrentUserId(username).compareTo(account.getUser().getId()) != 0)
            throw new AccessDeniedException("No tienes permisos para agregar cuentas en este usuario");

        if (repository.findByDescriptionAndUser(account.getDescription(), account.getUser()).isPresent())
            throw new DuplicateKeyException("Ya tienes una cuenta con esta descripción");

        if (account.getBalance() == null)
            account.setBalance(BigDecimal.ZERO);

        return repository.save(account);
    }

    @Override
    public Optional<Account> findById(Long id, String username) {

        Optional<Account> accountOptional = repository.findById(id);

        Account dbAccount = accountOptional.orElseThrow();

        Long userId = dbAccount.getUser().getId();

        if (userId.compareTo(getCurrentUserId(username)) != 0)
            throw new AccessDeniedException("No tienes permisos para acceder a este recurso");

        return accountOptional;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Account> findByUserId(Long userId, String username) {

        if (userId.compareTo(getCurrentUserId(username)) != 0)
            throw new AccessDeniedException("No tienes acceso a las cuentas de este usuario");

        return repository.findByUserId(userId);
    }

    @Transactional
    @Override
    public Account update(Long id, Account account, String username) {

        Account dbAccount = repository.findById(id).orElseThrow();

        Long userId = getCurrentUserId(username);

        if (userId.compareTo(dbAccount.getUser().getId()) != 0)
            throw new AccessDeniedException("No tienes permiso para editar esta cuenta");

        if (!account.getDescription().equalsIgnoreCase(dbAccount.getDescription())) {
            if (repository.findByDescriptionAndUser(account.getDescription(), dbAccount.getUser()).isPresent())
                throw new DuplicateKeyException("Ya tienes una cuenta con esta descripción");
        }

        dbAccount.setDescription(account.getDescription());

        return repository.save(dbAccount);
    }

    @Transactional
    @Override
    public void delete(Long id, String username) {

        Account account = repository.findById(id).orElseThrow();

        Long userId = account.getUser().getId();

        if (userId.compareTo(getCurrentUserId(username)) != 0)
            throw new AccessDeniedException("No puedes eliminar este recurso");

        repository.delete(account);
    }

    @Transactional(readOnly = true)
    private Long getCurrentUserId(String username) {
        return userRepository.findByUsername(username).orElseThrow().getId();
    }
}
