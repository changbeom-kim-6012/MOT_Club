package com.erns.mot.service;

import com.erns.mot.domain.Answer;
import com.erns.mot.domain.Question;
import com.erns.mot.repository.AnswerRepository;
import com.erns.mot.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public Optional<Answer> createAnswer(Long questionId, Answer answer) {
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if (questionOptional.isPresent()) {
            Question question = questionOptional.get();
            answer.setQuestion(question);
            
            // authorEmail이 설정되지 않은 경우 authorId를 사용
            if (answer.getAuthorEmail() == null && answer.getAuthorId() != null) {
                answer.setAuthorEmail(answer.getAuthorId());
            }
            
            Answer savedAnswer = answerRepository.save(answer);
            
            // 질문의 답변 개수 업데이트
            question.setAnswerCount(question.getAnswerCount() + 1);
            questionRepository.save(question);
            
            return Optional.of(savedAnswer);
        } else {
            return Optional.empty();
        }
    }

    public List<Answer> getAnswersByQuestionId(Long questionId) {
        return answerRepository.findByQuestionId(questionId);
    }

    @Transactional
    public boolean deleteAnswer(Long id) {
        Optional<Answer> answerOptional = answerRepository.findById(id);
        if (answerOptional.isPresent()) {
            Answer answer = answerOptional.get();
            Question question = answer.getQuestion();
            
            // 답변 삭제
            answerRepository.deleteById(id);
            
            // 질문의 답변 개수 업데이트
            if (question != null) {
                question.setAnswerCount(Math.max(0, question.getAnswerCount() - 1));
                questionRepository.save(question);
            }
            
            return true;
        }
        return false;
    }

    @Transactional
    public Optional<Answer> updateAnswer(Long id, Answer answerDetails) {
        Optional<Answer> answerOptional = answerRepository.findById(id);
        if (answerOptional.isPresent()) {
            Answer answer = answerOptional.get();
            answer.setContent(answerDetails.getContent());
            answer.setUpdatedAt(LocalDateTime.now());
            return Optional.of(answerRepository.save(answer));
        }
        return Optional.empty();
    }

    // 기존의 다른 메소드들은 컨트롤러에서 사용하지 않으므로 일단 제거하거나 주석처리 할 수 있습니다.
    // 여기서는 삭제하겠습니다.
} 