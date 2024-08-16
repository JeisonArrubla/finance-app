package com.jeison.finance.finance_app.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import jakarta.validation.Valid;

import com.jeison.finance.finance_app.models.User;
import com.jeison.finance.finance_app.services.UserService;

import static com.jeison.finance.finance_app.util.CommonUtils.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors())
            return validation(bindingResult);

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(user));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult bindingResult) {

        user.setAdmin(false);

        return create(user, bindingResult);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {

        return ResponseEntity.ok().body(service.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado")));
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll() {

        List<User> users = service.findAll();

        if (users.isEmpty())
            throw new NullPointerException("No hay usuarios registrados");

        return ResponseEntity.ok().body(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody User user, BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) return validation(bindingResult);

        return ResponseEntity.status(HttpStatus.CREATED).body(service.update(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
