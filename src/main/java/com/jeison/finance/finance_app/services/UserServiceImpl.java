package com.jeison.finance.finance_app.services;

import com.jeison.finance.finance_app.models.Role;
import com.jeison.finance.finance_app.models.User;
import com.jeison.finance.finance_app.repositories.RoleRepository;
import com.jeison.finance.finance_app.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User create(User user) {

        if (repository.existsByUsername(user.getUsername()))
            throw new DuplicateKeyException("El nombre de usuario no esta disponible");

        List<Role> roles = new ArrayList<>();

        Optional<Role> userRoleOptional = roleRepository.findByName("ROLE_USER");
        userRoleOptional.ifPresent(roles::add);

        if (user.isAdmin() != null) {
            if (user.isAdmin()) {
                Optional<Role> adminRoleOptional = roleRepository.findByName("ROLE_ADMIN");
                adminRoleOptional.ifPresent(roles::add);
            }
        }

        user.setUsername(user.getUsername().toLowerCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);

        return repository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(Long id, String username) {

        // Compara el id con el id del usuario que envía la petición
        // El id del usuario que envía la petición se obtiene con el username
        // NOTA: El username se utiliza como sujeto (subject) del
        // token y también se incluye en los claims del token.
        if (id.compareTo(repository.findByUsername(username).orElseThrow().getId()) != 0)
            throw new AccessDeniedException("No tienes permisos para acceder a este recurso");

        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {

        List<User> users = new ArrayList<>();

        repository.findAll().forEach(users::add);

        return users;
    }

    @Transactional
    @Override
    public User update(Long id, User user, String username) {

        if (id.compareTo(repository.findByUsername(username).orElseThrow().getId()) != 0)
            throw new AccessDeniedException("No tienes permisos para actualizar este usuario");

        Optional<User> userOptional = repository.findById(id);

        if (userOptional.isEmpty())
            throw new NoSuchElementException("No se encontró el usuario con ID " + id);

        User userDb = userOptional.orElseThrow();

        // Valida que el username no esté en uso cuando sea diferente al actual
        if (user.getUsername() != null) {
            if (!user.getUsername().equalsIgnoreCase(userDb.getUsername())) {
                if (repository.existsByUsername(user.getUsername()))
                    throw new DuplicateKeyException("El nombre de usuario ya esta en uso");

                userDb.setUsername(user.getUsername());
            }
        }

        if (user.getPassword() != null) {

            userDb.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return repository.save(userDb);
    }

    @Transactional
    @Override
    public void delete(Long id, String username) {

        if (id.compareTo(repository.findByUsername(username).orElseThrow().getId()) != 0)
            throw new AccessDeniedException("No tienes permisos para eliminar este recurso");

        repository.delete(repository.findById(id).orElseThrow());
    }
}
