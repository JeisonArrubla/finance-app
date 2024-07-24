package com.jeison.finance.finance_app.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.jeison.finance.finance_app.models.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
