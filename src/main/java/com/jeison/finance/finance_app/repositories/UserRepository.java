package com.jeison.finance.finance_app.repositories;

import com.jeison.finance.finance_app.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
