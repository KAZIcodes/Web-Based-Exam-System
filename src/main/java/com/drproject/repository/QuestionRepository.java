package com.drproject.repository;

import com.drproject.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.jaas.JaasPasswordCallbackHandler;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
