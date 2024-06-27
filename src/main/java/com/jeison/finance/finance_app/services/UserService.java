package com.jeison.finance.finance_app.services;

import com.jeison.finance.finance_app.models.User;
import com.jeison.finance.finance_app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<User> findAllUsers() {
        return (List<User>) repository.findAll();
    }
}
