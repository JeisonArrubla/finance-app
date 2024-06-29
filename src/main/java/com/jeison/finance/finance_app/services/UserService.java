package com.jeison.finance.finance_app.services;

import com.jeison.finance.finance_app.exceptions.NullFieldException;
import com.jeison.finance.finance_app.models.User;
import com.jeison.finance.finance_app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        if (users.isEmpty())
            throw new NullPointerException("No hay usuarios registrados");
        return users;
    }

    public User saveUser(User user) {
        if (user == null)
            throw new NullPointerException("El usuario no puede ser nulo");
        if (user.getUsername() == null || user.getUsername().isBlank())
            throw new NullFieldException("El nombre de usuario no puede ser nulo");
        if (userRepository.findByUsername(user.getUsername()).isPresent())
            throw new DuplicateKeyException("El nombre de usuario ya existe");
        return userRepository.save(user);
    }
}
