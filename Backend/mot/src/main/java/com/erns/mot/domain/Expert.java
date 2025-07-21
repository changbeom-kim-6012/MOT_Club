package com.erns.mot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "expert")
public class Expert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String organization;

    @Column(length = 100)
    private String position;

    @Column(columnDefinition = "TEXT")
    private String education; // 학력

    @Column(columnDefinition = "TEXT")
    private String career; // 경력

    @Column(columnDefinition = "TEXT")
    private String field; // 전문분야

    @Column(length = 20)
    private String status = "ACTIVE";

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime lastLogin;
    private LocalDateTime updatedAt = LocalDateTime.now();

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