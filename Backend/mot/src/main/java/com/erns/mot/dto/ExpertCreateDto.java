package com.erns.mot.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpertCreateDto {
    private String name;
    private String email;
    private String phone;
    private String organization;
    private String position;
    private String education;
    private String career;
    private String field;
    private String status = "ACTIVE";
} 