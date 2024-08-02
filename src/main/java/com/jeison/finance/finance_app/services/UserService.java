package com.jeison.finance.finance_app.services;

import java.util.List;
import java.util.Optional;

import com.jeison.finance.finance_app.models.User;

public interface UserService {

    User create(User user);

    Optional<User> findById(Long id, String username);

    List<User> findAll();

    User update(Long id, User user, String username);

    void delete(Long id, String username);
}
