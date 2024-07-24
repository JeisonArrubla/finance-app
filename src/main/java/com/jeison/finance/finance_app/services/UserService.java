package com.jeison.finance.finance_app.services;

import com.jeison.finance.finance_app.dto.UserDto;
import com.jeison.finance.finance_app.exceptions.NullFieldException;
import com.jeison.finance.finance_app.models.Role;
import com.jeison.finance.finance_app.models.User;
import com.jeison.finance.finance_app.repositories.RoleRepository;
import com.jeison.finance.finance_app.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Expresión regular que valida caracteres permitidos
    // Letras de la a-z números y guiones
    private static final String REGEX = "^[a-zA-Z0-9-]+$";

    @Transactional(readOnly = true)
    public List<UserDto> findAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        if (users.isEmpty())
            throw new NullPointerException("No hay usuarios registrados");
        List<UserDto> usersDto = users.stream()
                .map(u -> new UserDto(u.getId(), u.getUsername()))
                .toList();
        return usersDto;
    }

    @Transactional
    public Map<String, String> createUser(User user) {
        if (user == null)
            throw new NullPointerException("El usuario no puede ser nulo");
        if (user.getUsername() == null || user.getUsername().isBlank())
            throw new NullFieldException("El nombre de usuario no puede ser nulo");
        if (user.getUsername().length() < 4 || user.getUsername().length() > 15)
            throw new IllegalArgumentException("El nombre de usuario no cumple con la cantidad de caracteres");
        if (!Pattern.matches(REGEX, user.getUsername()))
            throw new IllegalArgumentException(
                    "El nombre de usuario no puede contener espacios o caracteres especiales");
        if (user.getPassword().length() < 8)
            throw new IllegalArgumentException("La contraseña debe tener mínimo 8 caracteres");
        if (userRepository.findByUsername(user.getUsername()).isPresent())
            throw new DuplicateKeyException("El nombre de usuario ya existe");
        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        optionalRoleUser.ifPresent(roles::add);
        if (user.isAdmin() != null) {
            if (user.isAdmin()) {
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
    public Map<String, String> updateUser(User user) {
        if (user.getId() == null)
            throw new NullPointerException("El id no puede ser nulo");
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (optionalUser.isPresent()) {
            user.setAccounts(optionalUser.get().getAccounts());
            userRepository.save(user);
            return Collections.singletonMap("message", "Usuario actualizado con éxito");
        }
        throw new EntityNotFoundException("Error al actualizar el usuario, inténtalo de nuevo");
    }

    @Transactional
    public Map<String, String> deleteUser(User user) {
        if (userRepository.existsById(user.getId())) {
            userRepository.delete(user);
            return Collections.singletonMap("message", "Usuario eliminado con éxito");
        }
        throw new EntityNotFoundException("Error al eliminar el usuario, inténtalo de nuevo");
    }
}
