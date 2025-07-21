package com.erns.mot.controller;

import com.erns.mot.dto.ExpertCreateDto;
import com.erns.mot.dto.ExpertDto;
import com.erns.mot.dto.ExpertUpdateDto;
import com.erns.mot.service.ExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experts")
@CrossOrigin(origins = "*")
public class ExpertController {
    private final ExpertService expertService;

    @Autowired
    public ExpertController(ExpertService expertService) {
        this.expertService = expertService;
    }

    // 전문가 생성
    @PostMapping
    public ResponseEntity<ExpertDto> createExpert(@RequestBody ExpertCreateDto expertDto) {
        try {
            ExpertDto createdExpert = expertService.createExpert(expertDto);
            return ResponseEntity.ok(createdExpert);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 전문가 수정
    @PutMapping("/{id}")
    public ResponseEntity<ExpertDto> updateExpert(@PathVariable Long id, @RequestBody ExpertUpdateDto expertDto) {
        try {
            ExpertDto updatedExpert = expertService.updateExpert(id, expertDto);
            return ResponseEntity.ok(updatedExpert);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 전문가 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpert(@PathVariable Long id) {
        try {
            expertService.deleteExpert(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 전문가 조회 (단일)
    @GetMapping("/{id}")
    public ResponseEntity<ExpertDto> getExpert(@PathVariable Long id) {
        try {
            ExpertDto expert = expertService.getExpert(id);
            return ResponseEntity.ok(expert);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 모든 전문가 조회 (관리자용)
    @GetMapping
    public ResponseEntity<List<ExpertDto>> getAllExperts() {
        List<ExpertDto> experts = expertService.getAllExperts();
        return ResponseEntity.ok(experts);
    }

    // 활성 전문가만 조회 (공개용)
    @GetMapping("/active")
    public ResponseEntity<List<ExpertDto>> getActiveExperts() {
        List<ExpertDto> experts = expertService.getActiveExperts();
        return ResponseEntity.ok(experts);
    }

    // 복합 검색
    @GetMapping("/search")
    public ResponseEntity<List<ExpertDto>> searchExperts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String organization,
            @RequestParam(required = false) String field,
            @RequestParam(required = false) String status) {
        
        List<ExpertDto> experts = expertService.searchExperts(name, email, organization, field, status);
        return ResponseEntity.ok(experts);
    }

    // 이름으로 검색
    @GetMapping("/search/name")
    public ResponseEntity<List<ExpertDto>> searchByName(@RequestParam String name) {
        List<ExpertDto> experts = expertService.searchByName(name);
        return ResponseEntity.ok(experts);
    }

    // 조직으로 검색
    @GetMapping("/search/organization")
    public ResponseEntity<List<ExpertDto>> searchByOrganization(@RequestParam String organization) {
        List<ExpertDto> experts = expertService.searchByOrganization(organization);
        return ResponseEntity.ok(experts);
    }

    // 전문분야로 검색
    @GetMapping("/search/field")
    public ResponseEntity<List<ExpertDto>> searchByField(@RequestParam String field) {
        List<ExpertDto> experts = expertService.searchByField(field);
        return ResponseEntity.ok(experts);
    }

    // 상태별 필터링
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ExpertDto>> getExpertsByStatus(@PathVariable String status) {
        List<ExpertDto> experts = expertService.getExpertsByStatus(status);
        return ResponseEntity.ok(experts);
    }

    // 로그인 시간 업데이트
    @PutMapping("/{id}/login")
    public ResponseEntity<Void> updateLastLogin(@PathVariable Long id) {
        try {
            expertService.updateLastLogin(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
} 