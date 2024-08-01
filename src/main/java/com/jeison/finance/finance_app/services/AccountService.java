package com.jeison.finance.finance_app.services;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeison.finance.finance_app.exceptions.NullFieldException;
import com.jeison.finance.finance_app.exceptions.ResourceNotFoundException;
import com.jeison.finance.finance_app.models.Account;
import com.jeison.finance.finance_app.repositories.AccountRepository;
import com.jeison.finance.finance_app.repositories.UserRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Account> findByUserId(Long userId, String currentUsername) {
        if (!userRepository.findById(userId).orElseThrow().getUsername().equals(currentUsername))
            throw new AccessDeniedException("No tienes acceso a las cuentas de este usuario");
        return repository.findByUserId(userId);
    }

    @Transactional
    public Account createAccount(Account account) {
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

        return repository.save(account);
    }

    @Transactional
    public Account update(Long id, Account account, String currentUsername) {
        Account dbAccount = repository.findById(id).orElseThrow();
        if (!dbAccount.getUser().getUsername().equals(currentUsername))
            throw new AccessDeniedException("No tienes permiso para editar esta cuenta");
        dbAccount.setDescription(account.getDescription());
        return repository.save(dbAccount);
    }

    @Transactional
    public Map<String, String> delete(Long id) {
        repository.delete(repository.findById(id).orElseThrow());
        return Collections.singletonMap("message", "Cuenta eliminada con éxito");
    }
}
