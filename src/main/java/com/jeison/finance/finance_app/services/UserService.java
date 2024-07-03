package com.jeison.finance.finance_app.services;

import com.jeison.finance.finance_app.dto.UserDto;
import com.jeison.finance.finance_app.exceptions.NullFieldException;
import com.jeison.finance.finance_app.models.User;
import com.jeison.finance.finance_app.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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

    public Map<String, String> createUser(User user) {
        if (user == null)
            throw new NullPointerException("El usuario no puede ser nulo");
        if (user.getUsername() == null || user.getUsername().isBlank())
            throw new NullFieldException("El nombre de usuario no puede ser nulo");
        if (userRepository.findByUsername(user.getUsername()).isPresent())
            throw new DuplicateKeyException("El nombre de usuario ya existe");
        userRepository.save(user);
        return Collections.singletonMap("message", "Usuario creado con éxito");
    }

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

    public Map<String, String> deleteUser(User user) {
        if (userRepository.existsById(user.getId())) {
            userRepository.delete(user);
            return Collections.singletonMap("message", "Usuario eliminado con éxito");
        }
        throw new EntityNotFoundException("Error al eliminar el usuario, inténtalo de nuevo");
    }
}
