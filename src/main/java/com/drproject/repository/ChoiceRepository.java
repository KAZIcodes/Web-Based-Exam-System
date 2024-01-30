package com.drproject.repository;

import com.drproject.entity.Choice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Jpa21Utils;
import org.springframework.data.repository.query.Param;

public interface ChoiceRepository extends JpaRepository<Choice, Long> {
    public Choice getChoiceById(@Param("UUID") String id);
}
