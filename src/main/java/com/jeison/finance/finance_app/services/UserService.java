package com.jeison.finance.finance_app.services;

import com.jeison.finance.finance_app.models.Role;
import com.jeison.finance.finance_app.models.User;
import com.jeison.finance.finance_app.repositories.RoleRepository;
import com.jeison.finance.finance_app.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        if (users.isEmpty())
            throw new NullPointerException("No hay usuarios registrados");
        return users;
    }

    @Transactional
    public Map<String, String> save(User user) {
        if (userRepository.existsByUsername(user.getUsername()))
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        optionalRoleUser.ifPresent(roles::add);
        if (user.getAdmin() != null) {
            if (user.getAdmin()) {
                Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
                optionalRoleAdmin.ifPresent(roles::add);
            }
        }
        user.setRoles(roles);
        user.setUsername(user.getUsername().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return Collections.singletonMap("message", "Usuario creado con éxito");
    }

    @Transactional
    public Optional<User> update(Long id, User user) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User userDb = userOptional.orElseThrow();
            userDb.setUsername(user.getUsername());
            return Optional.of(userRepository.save(userDb));
        }
        return userOptional;
    }

    @Transactional
    public Map<String, String> deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
            return Collections.singletonMap("message", "Usuario eliminado con éxito");
        }
        throw new EntityNotFoundException("Error al eliminar el usuario, inténtalo de nuevo");
    }
}
