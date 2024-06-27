package com.jeison.finance.finance_app.controllers;

import com.jeison.finance.finance_app.models.User;
import com.jeison.finance.finance_app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/show")
    public List<User> finAll() {
        return userService.finAll();
    }
}
