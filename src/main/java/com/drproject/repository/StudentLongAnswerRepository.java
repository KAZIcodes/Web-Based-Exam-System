package com.drproject.repository;

import com.drproject.entity.LongAnswer;
import com.drproject.entity.StudentLongAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

public interface StudentLongAnswerRepository extends JpaRepository<StudentLongAnswer,Long> {
    public StudentLongAnswer getStudentLongAnswerById(@Param("UUID") String uuid);
    public boolean existsById(@Param("UUID") String uuid);
}
