package com.jeison.finance.finance_app.services;

import com.jeison.finance.finance_app.models.User;
import com.jeison.finance.finance_app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User saveUser(User u) {
        if (u.getUsername() == null || u.getUsername().isBlank()) {
            System.out.println("Ingrese un nombre de usuario");
            return null;
        }
        if (userRepository.findByUsername(u.getUsername()).isPresent()) {
            System.err.println("Nombre de usuario no disponible");
            return null;
        }
        return userRepository.save(u);
    }
}
