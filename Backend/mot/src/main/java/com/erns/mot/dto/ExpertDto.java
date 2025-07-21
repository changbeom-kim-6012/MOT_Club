package com.erns.mot.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpertDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String organization;
    private String position;
    private String education;
    private String career;
    private String field;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private LocalDateTime updatedAt;
} 