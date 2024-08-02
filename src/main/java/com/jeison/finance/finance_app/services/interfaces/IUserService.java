package com.jeison.finance.finance_app.services.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.jeison.finance.finance_app.models.User;

public interface IUserService {

    User create(User user);

    User findById(Long id);

    List<User> findAll();

    Optional<User> update(Long id, User user);

    Map<String, String> delete(Long id);

    Long getIdByUsername(String username);
}
