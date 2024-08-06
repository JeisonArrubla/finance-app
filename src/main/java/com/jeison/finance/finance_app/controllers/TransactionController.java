package com.jeison.finance.finance_app.controllers;

import com.jeison.finance.finance_app.models.Transaction;
import com.jeison.finance.finance_app.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController extends BaseController {

    @Autowired
    private TransactionService service;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Transaction transaction, BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors())
            return validation(bindingResult);

        if (transaction.getSourceAccount() == null)
            throw new IllegalArgumentException("Debes indicar la cuenta origen");

        try {

            return ResponseEntity.status(HttpStatus.CREATED).body(service.create(transaction, getCurrentUsername()));

        } catch (NoSuchElementException e) {

            throw new NoSuchElementException("Cuenta inválida");

        }
    }
}
