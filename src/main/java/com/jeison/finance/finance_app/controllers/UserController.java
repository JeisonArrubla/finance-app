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

        Optional<User> userOptional = service.findById(id);

        try {

            return ResponseEntity.ok().body(userOptional.orElseThrow());

        } catch (NoSuchElementException e) {

            throw new NoSuchElementException("Usuario no encontrado");

        }
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

        if (bindingResult.hasFieldErrors())
            return validation(bindingResult);

        try {

            return ResponseEntity.status(HttpStatus.CREATED).body(service.update(id, user));

        } catch (NoSuchElementException e) {

            throw new NoSuchElementException("No se encontró el usuario, inténtalo de nuevo");

        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        try {

            service.delete(id);

            return ResponseEntity.noContent().build();

        } catch (NoSuchElementException e) {

            throw new NoSuchElementException("No se encontró el usuario");

        }
    }
}
