package com.drproject.repository;

import com.drproject.entity.Activity;
import com.drproject.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.UUID;

public interface ARRepository extends JpaRepository<Activity, Long> {
    boolean existsById(@Param("Id") String id);
    Activity getActivityById(@Param("Id") String id);
}
