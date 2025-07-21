package com.erns.mot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "attachment")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String refTable; // 참조 테이블명(community, questions, library 등)

    @Column(nullable = false)
    private Long refId; // 참조 테이블의 PK

    @Column(nullable = false, length = 255)
    private String fileName; // 원본 파일명

    @Column(nullable = false, length = 255)
    private String filePath; // 서버 저장 경로

    private Long fileSize; // 파일 크기(바이트)

    private String uploadedBy; // 업로더(선택)

    private LocalDateTime uploadedAt = LocalDateTime.now();
    @Column(length = 255)
    private String note; // 비고(설명)
} 