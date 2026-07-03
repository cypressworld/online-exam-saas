package com.exam.saas.repository;

import com.exam.saas.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    // Spring Data JPA will automatically implement all CRUD operations (count, findAll, saveAll, etc.)
}