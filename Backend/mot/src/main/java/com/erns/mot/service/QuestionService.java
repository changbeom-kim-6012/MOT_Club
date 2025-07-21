package com.erns.mot.service;

import com.erns.mot.domain.Question;
import com.erns.mot.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final Path qnaUploadDir;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
        this.qnaUploadDir = Paths.get("uploads/qna").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.qnaUploadDir);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    // 모든 질문 목록 조회 (Library 패턴과 동일)
    public List<Question> getAllQuestions() {
        return questionRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    // 새 질문 생성 (Library 패턴과 동일)
    public Question createQuestion(Question question, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String originalFileName = file.getOriginalFilename();
            String storedFileName = UUID.randomUUID().toString() + "_" + originalFileName;
            Path targetLocation = this.qnaUploadDir.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetLocation);
            question.setFilePath(storedFileName);
        }
        return questionRepository.save(question);
    }

    // ID로 특정 질문 조회
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    // 질문 수정 (Library 패턴과 동일)
    public Optional<Question> updateQuestion(Long id, Question questionDetails, MultipartFile file) throws IOException {
        return questionRepository.findById(id)
                .map(existingQuestion -> {
                    existingQuestion.setTitle(questionDetails.getTitle());
                    existingQuestion.setContent(questionDetails.getContent());
                    existingQuestion.setCategory1(questionDetails.getCategory1());
                    existingQuestion.setPublic(questionDetails.isPublic());
                    existingQuestion.setContactInfo(questionDetails.getContactInfo());
                    
                    if (file != null && !file.isEmpty()) {
                        try {
                            String originalFileName = file.getOriginalFilename();
                            String storedFileName = UUID.randomUUID().toString() + "_" + originalFileName;
                            Path targetLocation = this.qnaUploadDir.resolve(storedFileName);
                            Files.copy(file.getInputStream(), targetLocation);
                            existingQuestion.setFilePath(storedFileName);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to store file", e);
                        }
                    }
                    
                    return questionRepository.save(existingQuestion);
                });
    }

    // 질문 삭제
    public boolean deleteQuestion(Long id) {
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // 모든 질문의 답변 개수 업데이트 (데이터 정합성용)
    @Transactional
    public void updateAllAnswerCounts() {
        List<Question> questions = questionRepository.findAll();
        for (Question question : questions) {
            int actualAnswerCount = question.getAnswers().size();
            if (question.getAnswerCount() != actualAnswerCount) {
                question.setAnswerCount(actualAnswerCount);
                questionRepository.save(question);
            }
        }
    }

    // 특정 질문의 답변 개수 업데이트
    @Transactional
    public void updateAnswerCount(Long questionId) {
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if (questionOptional.isPresent()) {
            Question question = questionOptional.get();
            int actualAnswerCount = question.getAnswers().size();
            question.setAnswerCount(actualAnswerCount);
            questionRepository.save(question);
        }
    }
} 