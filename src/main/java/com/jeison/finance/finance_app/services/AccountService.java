package com.jeison.finance.finance_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Account a = new Account();
        a.setDescription(account.getDescription());
        a.setBalance(account.getBalance());
        // Validar si el usuario existe
        userRepository.findById(account.getUser().getId())
                .ifPresentOrElse(
                        a::setUser,
                        () -> a.setUser(null));
        if (a.getUser() == null) {
            System.err.println("No se encontró el usuario");
            return null;
        }
        // Validar que el usuario no tenga una cuenta con la misma descripción
        if (accountRepository.findByDescriptionAndUser(account.getDescription(), account.getUser()).isPresent()) {
            System.err.println("Nombre de cuenta ya existe");
            return null;
        }
        return accountRepository.save(a);
    }
}
