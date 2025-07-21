package com.erns.mot.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CommonCodeDto {
    private Long id;
    private String menuName;
    private String codeName;
    private String codeValue;
    private String description;
    private Integer sortOrder;
    private Long parentId; // 프론트에서 parentId로 전달
    private List<CommonCodeDto> children; // 계층구조 표현
} 