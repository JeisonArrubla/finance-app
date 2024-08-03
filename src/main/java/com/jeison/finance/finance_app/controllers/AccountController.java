package com.jeison.finance.finance_app.controllers;

import com.jeison.finance.finance_app.models.Account;
import com.jeison.finance.finance_app.services.AccountService;

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/v1/accounts")
public class AccountController {

    @Autowired
    private AccountService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Account account) {

        if (account.getUser() == null)
            throw new IllegalArgumentException("El usuario no puede ser nulo");

        if (account.getUser().getId() == null)
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");

        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(service.create(account, getCurrentUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> findById(@PathVariable Long id) {

        try {

            Account account = service.findById(id, getCurrentUsername()).orElseThrow();

            return ResponseEntity.ok().body(account);

        } catch (NoSuchElementException e) {

            throw new NoSuchElementException("No se encontró la cuenta con ID " + id);

        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> findByUserId(@PathVariable Long userId) {

        try {

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(service.findByUserId(userId, getCurrentUsername()));

        } catch (AccessDeniedException e) {

            throw new AccessDeniedException(e.getMessage());

        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
            @Valid @RequestBody Account account, BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors())
            return validation(bindingResult);

        try {

            return ResponseEntity
                    .status(HttpStatus.CREATED.value())
                    .body(service.update(id, account, getCurrentUsername()));

        } catch (NoSuchElementException e) {

            throw new NoSuchElementException("Cuenta no encontrada");

        } catch (AccessDeniedException e) {

            throw new AccessDeniedException(e.getMessage());

        } catch (DuplicateKeyException e) {

            throw new DuplicateKeyException(e.getMessage());

        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        try {
            service.delete(id, getCurrentUsername());
            return ResponseEntity
                    .status(HttpStatus.OK.value())
                    .body(Collections.singletonMap("message", "Cuenta eliminada con éxito"));
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Cuenta no encontrada");
        }
    }

    private ResponseEntity<Map<String, String>> validation(BindingResult bindingResult) {

        Map<String, String> errors = new HashMap<>();

        bindingResult.getFieldErrors()
                .forEach(fieldErrors -> errors.put(fieldErrors.getField(), fieldErrors.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
