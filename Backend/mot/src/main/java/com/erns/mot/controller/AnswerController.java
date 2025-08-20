package com.erns.mot.controller;

import com.erns.mot.domain.Answer;
import com.erns.mot.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:30001", "http://localhost:3001", "http://localhost:13000"})
public class AnswerController {

    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping("/questions/{questionId}/answers")
    public ResponseEntity<Answer> createAnswer(@PathVariable Long questionId, @RequestBody Answer answer) {
        return answerService.createAnswer(questionId, answer)
                .map(createdAnswer -> new ResponseEntity<>(createdAnswer, HttpStatus.CREATED))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/questions/{questionId}/answers")
    public ResponseEntity<List<Answer>> getAnswersByQuestionId(@PathVariable Long questionId) {
        List<Answer> answers = answerService.getAnswersByQuestionId(questionId);
        return ResponseEntity.ok(answers);
    }

    @DeleteMapping("/answers/{answerId}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long answerId) {
        boolean isDeleted = answerService.deleteAnswer(answerId);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/answers/{answerId}")
    public ResponseEntity<Answer> updateAnswer(@PathVariable Long answerId, @RequestBody Answer answerDetails) {
        Optional<Answer> updatedAnswer = answerService.updateAnswer(answerId, answerDetails);
        if (updatedAnswer.isPresent()) {
            return ResponseEntity.ok(updatedAnswer.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
} 