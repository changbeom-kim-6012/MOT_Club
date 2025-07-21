package com.erns.mot.repository;

import com.erns.mot.domain.Question;
import com.erns.mot.domain.QuestionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCategory1(String category1);
    List<Question> findByStatus(QuestionStatus status);
    List<Question> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);
}