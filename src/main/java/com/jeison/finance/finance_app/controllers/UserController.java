package com.jeison.finance.finance_app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
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

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors())
            return validation(bindingResult);

        try {

            return ResponseEntity.status(HttpStatus.CREATED).body(service.create(user));

        } catch (DuplicateKeyException e) {

            throw new DuplicateKeyException(e.getMessage());

        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult bindingResult) {

        user.setAdmin(false);

        return create(user, bindingResult);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {

        Optional<User> userOptional = service.findById(id, getCurrentUsername());

        try {

            return ResponseEntity.ok().body(userOptional.orElseThrow());

        } catch (NoSuchElementException e) {

            throw new NoSuchElementException("Usuario no encontrado");

        } catch (AccessDeniedException e) {

            throw new AccessDeniedException(e.getMessage());

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

            return ResponseEntity.status(HttpStatus.CREATED).body(service.update(id, user, getCurrentUsername()));

        } catch (NoSuchElementException e) {

            throw new NoSuchElementException("No se encontró el usuario, inténtalo de nuevo");

        } catch (DuplicateKeyException e) {

            throw new DuplicateKeyException(e.getMessage());

        } catch (AccessDeniedException e) {

            throw new AccessDeniedException(e.getMessage());

        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        try {

            service.delete(id, getCurrentUsername());

            return ResponseEntity.noContent().build();

        } catch (NoSuchElementException e) {

            throw new NoSuchElementException("No se encontró el usuario con ID " + id);

        } catch (AccessDeniedException e) {

            throw new AccessDeniedException(e.getMessage());

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
