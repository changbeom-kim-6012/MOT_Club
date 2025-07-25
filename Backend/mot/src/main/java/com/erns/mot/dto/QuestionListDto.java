package com.erns.mot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class QuestionListDto {
    private Long id;
    private String title;
    private String content;
    private String authorEmail;
    private LocalDateTime createdAt;
    private String category1;
    private int viewCount;
    private int answerCount;
    private String status;
    @JsonProperty("isPublic")
    private boolean isPublic;

    public QuestionListDto(Long id, String title, String content, String authorEmail, LocalDateTime createdAt, String category1, int viewCount, int answerCount, String status, boolean isPublic) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorEmail = authorEmail;
        this.createdAt = createdAt;
        this.category1 = category1;
        this.viewCount = viewCount;
        this.answerCount = answerCount;
        this.status = status;
        this.isPublic = isPublic;
    }
} 