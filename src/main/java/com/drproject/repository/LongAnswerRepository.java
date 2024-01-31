package com.drproject.repository;

import com.drproject.entity.LongAnswer;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

public interface LongAnswerRepository extends JpaRepository<LongAnswer, Long> {
    public LongAnswer getLongAnswerById(@Param("UUID") String id);
    public boolean existsById(@Param("UUID") String id);
}