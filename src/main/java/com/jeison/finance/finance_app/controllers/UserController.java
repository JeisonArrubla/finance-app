package com.jeison.finance.finance_app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import com.jeison.finance.finance_app.models.User;
import com.jeison.finance.finance_app.services.interfaces.IUserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private IUserService service;

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        Optional<User> userOptional = service.findById(id);
        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK.value())
                    .body(userOptional.orElseThrow());
        }
        throw new EntityNotFoundException("Usuario no encontrado");
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors())
            return validation(bindingResult);

        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(service.create(user));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult bindingResult) {
        user.setAdmin(false);
        return create(user, bindingResult);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors())
            return validation(bindingResult);
        Optional<User> userOptional = service.update(id, user);
        if (userOptional.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CREATED.value())
                    .body(userOptional.orElseThrow());
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deteleUser(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(service.delete(id));
    }

    private ResponseEntity<?> validation(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST.value())
                .body(errors);
    }
}
