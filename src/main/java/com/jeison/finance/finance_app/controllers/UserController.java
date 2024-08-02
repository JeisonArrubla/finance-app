package com.jeison.finance.finance_app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.jeison.finance.finance_app.exceptions.BadRequestException;
import com.jeison.finance.finance_app.models.User;
import com.jeison.finance.finance_app.services.interfaces.IUserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private IUserService service;

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        if (!idMatch(id))
            throw new AccessDeniedException("No tienes permisos para acceder a este recurso");
        try {
            return ResponseEntity
                    .status(HttpStatus.OK.value())
                    .body(service.findById(id));
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Usuario no encontrado");
        } catch (Exception e) {
            throw new BadRequestException("Error al consultar el usuario");
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(service.findAll());
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
        if (!idMatch(id))
            throw new AccessDeniedException("No tienes permisos para actualizar este usuario");
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

    // Compara un id con el id del usuario que hace la petición
    private boolean idMatch(Long id) {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        Optional<User> userOptional = service.getUserByUsername(username);
        if (userOptional.isPresent()) {
            return userOptional.orElseThrow().getId().compareTo(id) == 0;
        }

        // Esta excepción se produce cuando se actualiza el nombre de usuario pero el
        // token no se ha actualizado.
        // NOTA: El nombre de usuario (username) se utiliza como sujeto (subject) del
        // token y también se incluye en los claims del token.
        throw new NoSuchElementException("No se encontró el usuario " + username);
    }
}
