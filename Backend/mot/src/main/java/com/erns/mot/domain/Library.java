package com.erns.mot.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "library")
@Getter
@Setter
@NoArgsConstructor
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String title;
    private String author;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String keywords;
    
    @Column(columnDefinition = "TEXT")
    private String fileNames; // 다중 파일명을 쉼표로 구분하여 저장
    
    @Column(columnDefinition = "TEXT")
    private String filePaths; // 다중 파일 경로를 쉼표로 구분하여 저장
    
    @Column(columnDefinition = "TEXT")
    private String fileTypes; // 다중 파일 타입을 쉼표로 구분하여 저장 (view-only, downloadable)

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

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getFileNames() { return fileNames; }
    public void setFileNames(String fileNames) { this.fileNames = fileNames; }
    public String getFilePaths() { return filePaths; }
    public void setFilePaths(String filePaths) { this.filePaths = filePaths; }
    public String getFileTypes() { return fileTypes; }
    public void setFileTypes(String fileTypes) { this.fileTypes = fileTypes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
} 