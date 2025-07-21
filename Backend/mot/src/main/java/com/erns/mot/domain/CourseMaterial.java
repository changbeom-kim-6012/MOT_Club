package com.erns.mot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "course_materials")
@Getter
@Setter
@NoArgsConstructor
public class CourseMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String courseId; // 과정 ID (예: "1", "2")

    @Column(nullable = false)
    private String sectionId; // 섹션 ID (예: "I", "II", "III")

    @Column(nullable = false)
    private String topicId; // 토픽 ID (예: "1", "2", "3")

    @Column(nullable = false)
    private String itemId; // 아이템 ID (예: "1", "2", "3", "4")

    @Column(nullable = false)
    private String title; // 자료 제목

    @Column(columnDefinition = "TEXT")
    private String description; // 자료 설명

    private String fileName; // 원본 파일명
    private String filePath; // 서버 저장 경로
    private Long fileSize; // 파일 크기

    @Column(length = 100)
    private String uploadedBy; // 업로더

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 