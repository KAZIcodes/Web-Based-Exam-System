package com.drproject.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.drproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Custom methods if needed

    boolean existsByUsername(@Param("username") String username);

    boolean existsByEmail(@Param("email") String email);
    User getUserByEmail(@Param("email") String email);
    User getUserByUsername(@Param("username") String username);
}
