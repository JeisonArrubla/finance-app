package com.jeison.finance.finance_app.services;

import com.jeison.finance.finance_app.models.Role;
import com.jeison.finance.finance_app.models.User;
import com.jeison.finance.finance_app.repositories.RoleRepository;
import com.jeison.finance.finance_app.repositories.UserRepository;
import com.jeison.finance.finance_app.services.interfaces.IUserService;

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
public class UserService implements IUserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        repository.findAll().forEach(users::add);
        if (users.isEmpty())
            throw new NullPointerException("No hay usuarios registrados");
        return users;
    }

    @Transactional
    @Override
    public User create(User user) {
        if (repository.existsByUsername(user.getUsername()))
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        Optional<Role> userRoleOptional = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        userRoleOptional.ifPresent(roles::add);
        if (user.getAdmin() != null) {
            if (user.getAdmin()) {
                Optional<Role> adminRoleOptional = roleRepository.findByName("ROLE_ADMIN");
                adminRoleOptional.ifPresent(roles::add);
            }
        }
        user.setRoles(roles);
        user.setUsername(user.getUsername().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Transactional
    @Override
    public Optional<User> update(Long id, User user) {
        Optional<User> userOptional = repository.findById(id);
        if (userOptional.isPresent()) {
            User userDb = userOptional.orElseThrow();
            if (user.getUsername() != null) {
                userDb.setUsername(user.getUsername());
            }
            if (user.getPassword() != null) {
                userDb.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            return Optional.of(repository.save(userDb));
        }
        return userOptional;
    }

    @Transactional
    @Override
    public Map<String, String> delete(Long id) {
        Optional<User> userOptional = repository.findById(id);
        if (userOptional.isPresent()) {
            repository.delete(userOptional.get());
            return Collections.singletonMap("message", "Usuario eliminado con éxito");
        }
        throw new EntityNotFoundException("Error al eliminar el usuario, inténtalo de nuevo");
    }
}
