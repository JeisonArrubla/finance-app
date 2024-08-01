package com.jeison.finance.finance_app.controllers;

import com.jeison.finance.finance_app.exceptions.BadRequestException;
import com.jeison.finance.finance_app.models.Account;
import com.jeison.finance.finance_app.services.AccountService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/v1/accounts")
public class AccountController {

    @Autowired
    private AccountService service;

    @PostMapping
    public ResponseEntity<Map<String, String>> createAccount(@RequestBody Account account) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createAccount(account));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUserId(@PathVariable Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAccountsByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Long id,
            @Valid @RequestBody Account account) {
        Optional<Account> accountOptional = service.update(id, account);
        if (accountOptional.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(accountOptional.orElseThrow());
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteAccount(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK.value())
                    .body(Collections.singletonMap("message", "Cuenta eliminada con éxito"));
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Cuenta no encontrada");
        } catch (Exception e) {
            throw new BadRequestException("Error al eliminar la cuenta");
        }
    }
}
