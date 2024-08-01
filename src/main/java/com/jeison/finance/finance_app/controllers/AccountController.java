package com.jeison.finance.finance_app.controllers;

import com.jeison.finance.finance_app.exceptions.BadRequestException;
import com.jeison.finance.finance_app.models.Account;
import com.jeison.finance.finance_app.services.AccountService;

import jakarta.validation.Valid;

import java.util.Collections;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/v1/accounts")
public class AccountController {

    @Autowired
    private AccountService service;

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(service.createAccount(account));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Account>> findByUserId(@PathVariable Long userId) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK.value())
                    .body(service.findByUserId(
                            userId,
                            SecurityContextHolder
                                    .getContext()
                                    .getAuthentication()
                                    .getName()));
        } catch (NoSuchElementException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
            @Valid @RequestBody Account account) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED.value())
                    .body(service.update(
                            id,
                            account,
                            SecurityContextHolder
                                    .getContext()
                                    .getAuthentication()
                                    .getName()));
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Cuenta no encontrada");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK.value())
                    .body(Collections.singletonMap("message", "Cuenta eliminada con éxito"));
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Cuenta no encontrada");
        } catch (Exception e) {
            throw new BadRequestException("Error al eliminar la cuenta");
        }
    }
}
