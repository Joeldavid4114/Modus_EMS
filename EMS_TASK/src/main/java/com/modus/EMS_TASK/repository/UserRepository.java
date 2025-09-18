package com.modus.EMS_TASK.repository;

import com.modus.EMS_TASK.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<Object> findByUsername(String username);
}
