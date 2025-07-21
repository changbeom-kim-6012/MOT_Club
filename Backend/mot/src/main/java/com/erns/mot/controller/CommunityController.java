package com.erns.mot.controller;

import com.erns.mot.dto.CommunityDto;
import com.erns.mot.service.CommunityService;
import com.erns.mot.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/community")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:13000"})
public class CommunityController {
    private final CommunityService communityService;
    private final AttachmentService attachmentService;

    @Autowired
    public CommunityController(CommunityService communityService, AttachmentService attachmentService) {
        this.communityService = communityService;
        this.attachmentService = attachmentService;
    }

    // 모든 커뮤니티 게시글 조회
    @GetMapping
    public ResponseEntity<List<CommunityDto>> getAll() {
        List<CommunityDto> communities = communityService.findAll();
        return ResponseEntity.ok(communities);
    }

    // ID로 커뮤니티 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity<CommunityDto> getById(@PathVariable Long id) {
        CommunityDto community = communityService.findById(id);
        if (community == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(community);
    }

    // 카테고리별 커뮤니티 게시글 조회
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CommunityDto>> getByCategory(@PathVariable Long categoryId) {
        List<CommunityDto> communities = communityService.findByCategoryId(categoryId);
        return ResponseEntity.ok(communities);
    }

    // 검색
    @GetMapping("/search")
    public ResponseEntity<List<CommunityDto>> search(@RequestParam String q) {
        List<CommunityDto> communities = communityService.searchByTitleOrContent(q);
        return ResponseEntity.ok(communities);
    }

    // 커뮤니티 게시글 생성
    @PostMapping
    public ResponseEntity<CommunityDto> create(@RequestBody CommunityDto communityDto) {
        CommunityDto saved = communityService.save(communityDto);
        return ResponseEntity.ok(saved);
    }

    // 커뮤니티 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<CommunityDto> update(@PathVariable Long id, @RequestBody CommunityDto communityDto) {
        CommunityDto updated = communityService.update(id, communityDto);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // 커뮤니티 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        communityService.delete(id);
        attachmentService.deleteFiles("community", id);
        return ResponseEntity.noContent().build();
    }
} 