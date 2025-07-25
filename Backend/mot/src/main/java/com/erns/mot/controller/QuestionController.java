package com.erns.mot.controller;

import com.erns.mot.domain.Question;
import com.erns.mot.dto.QuestionListDto;
import com.erns.mot.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:13000"})
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // 모든 질문 목록 조회
    @GetMapping
    public ResponseEntity<List<QuestionListDto>> getAllQuestions() {
        List<Question> questions = questionService.getAllQuestions();
        List<QuestionListDto> dtos = questions.stream()
            .map(q -> new QuestionListDto(
                q.getId(), 
                q.getTitle(), 
                q.getContent(), 
                q.getAuthorEmail(), 
                q.getCreatedAt(), 
                q.getCategory1(), 
                q.getViewCount(), 
                q.getAnswerCount(), 
                q.getStatus().toString(), 
                q.isPublic()
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // 새 질문 생성 (Library 패턴과 동일)
    @PostMapping
    public ResponseEntity<Question> createQuestion(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("category1") String category1,
            @RequestParam("authorEmail") String authorEmail,
            @RequestParam(value = "isPublic", defaultValue = "true") String isPublicStr,
            @RequestParam(value = "contactInfo", required = false) String contactInfo,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            // 문자열을 boolean으로 변환
            boolean isPublic = Boolean.parseBoolean(isPublicStr);
            
            // 디버깅을 위한 로그 추가
            System.out.println("받은 isPublic 문자열: " + isPublicStr);
            System.out.println("변환된 isPublic boolean: " + isPublic);
            
            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCategory1(category1);
            question.setAuthorEmail(authorEmail);
            question.setPublic(isPublic);
            question.setContactInfo(contactInfo);

            // 설정된 값 확인
            System.out.println("Question에 설정된 isPublic: " + question.isPublic());

            Question createdQuestion = questionService.createQuestion(question, file);
            return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ID로 특정 질문 조회
    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 질문 수정
    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("category1") String category1,
            @RequestParam(value = "isPublic", defaultValue = "true") String isPublicStr,
            @RequestParam(value = "contactInfo", required = false) String contactInfo,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            // 문자열을 boolean으로 변환
            boolean isPublic = Boolean.parseBoolean(isPublicStr);
            
            Question questionDetails = new Question();
            questionDetails.setTitle(title);
            questionDetails.setContent(content);
            questionDetails.setCategory1(category1);
            questionDetails.setPublic(isPublic);
            questionDetails.setContactInfo(contactInfo);

            return questionService.updateQuestion(id, questionDetails, file)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 질문 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        boolean isDeleted = questionService.deleteQuestion(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 모든 질문의 답변 개수 업데이트 (관리자용)
    @PostMapping("/update-answer-counts")
    public ResponseEntity<String> updateAllAnswerCounts() {
        try {
            questionService.updateAllAnswerCounts();
            return ResponseEntity.ok("답변 개수가 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("답변 개수 업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
} 