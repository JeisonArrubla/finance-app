package com.jeison.finance.finance_app.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.jeison.finance.finance_app.models.User;

public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
