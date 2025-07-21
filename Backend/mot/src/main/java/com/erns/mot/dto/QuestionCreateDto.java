package com.erns.mot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionCreateDto {
    private String title;
    private String content;
    private String category1;
    private String authorEmail;
    private boolean isPublic = true;
    private String contactInfo; // 연락처 필드 추가
} 