package com.jeison.finance.finance_app.controllers;

import com.jeison.finance.finance_app.models.Account;
import com.jeison.finance.finance_app.services.AccountService;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountService.saveAccount(account));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUserId(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountService.getAccountsByUserId(userId));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteAccount(@RequestBody Account account) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountService.deleteAccount(account));
    }
}
