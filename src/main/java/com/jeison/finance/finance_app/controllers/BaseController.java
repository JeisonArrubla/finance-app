package com.jeison.finance.finance_app.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

public class BaseController {

    protected ResponseEntity<Map<String, String>> validation(BindingResult bindingResult) {

        Map<String, String> errors = new HashMap<>();

        bindingResult.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }

    protected String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
