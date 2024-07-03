package com.jeison.finance.finance_app.controllers;

import com.jeison.finance.finance_app.dto.UserDto;
import com.jeison.finance.finance_app.models.User;
import com.jeison.finance.finance_app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/show")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findAllUsers());
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> createUser(@RequestBody User user) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(user));
    }

    @PatchMapping("/update")
    public ResponseEntity<Map<String, String>> updateUser(@RequestBody User user) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.updateUser(user));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deteleUser(@RequestBody User user) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.deleteUser(user));
    }
}
