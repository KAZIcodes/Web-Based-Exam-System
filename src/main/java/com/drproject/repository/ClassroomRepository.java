package com.drproject.repository;
import com.drproject.entity.Classroom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.drproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    boolean existsByCode(@Param("code")UUID code);
    Classroom getClassroomByCode(@Param("code")UUID code);

}