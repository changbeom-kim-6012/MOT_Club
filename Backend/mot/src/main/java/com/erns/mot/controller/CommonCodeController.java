package com.erns.mot.controller;

import com.erns.mot.domain.CommonCode;
import com.erns.mot.service.CommonCodeService;
import com.erns.mot.dto.CommonCodeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/api/codes")
@RequiredArgsConstructor
public class CommonCodeController {

    private final CommonCodeService commonCodeService;

    @GetMapping
    public ResponseEntity<List<CommonCodeDto>> getAllCodes(
            @RequestParam(value = "parentId", required = false) Long parentId,
            @RequestParam(value = "menuName", required = false) String menuName) {
        if (parentId != null) {
            return ResponseEntity.ok(commonCodeService.findByParentIdDto(parentId));
        } else {
            return ResponseEntity.ok(commonCodeService.findAllDto());
        }
    }

    @PostMapping
    public ResponseEntity<CommonCode> createCode(@RequestBody CommonCodeDto dto) {
        CommonCode savedCode = commonCodeService.save(dto);
        return ResponseEntity.status(201).body(savedCode);
    }

    @PostMapping("/dummy")
    public ResponseEntity<String> createDummyData() {
        commonCodeService.createDummyData();
        return ResponseEntity.ok("더미 데이터가 생성되었습니다.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonCode> updateCode(@PathVariable Long id, @RequestBody CommonCodeDto dto) {
        CommonCode code = commonCodeService.update(id, dto);
        return ResponseEntity.ok(code);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCode(@PathVariable Long id) {
        commonCodeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/library-masters")
    public ResponseEntity<List<CommonCodeDto>> getLibraryMasterCodes() {
        return ResponseEntity.ok(commonCodeService.findLibraryMasterCodes());
    }

    @GetMapping("/library-details")
    public ResponseEntity<List<CommonCodeDto>> getLibraryDetailCodes() {
        return ResponseEntity.ok(commonCodeService.findLibraryDetailCodes());
    }

    // Agora 카테고리 조회 (Library 패턴과 동일)
    @GetMapping("/agora-details")
    public ResponseEntity<List<CommonCodeDto>> getAgoraDetailCodes() {
        return ResponseEntity.ok(commonCodeService.findAgoraDetailCodes());
    }

    // 통합된 메뉴별 하위 코드 조회
    @GetMapping("/menu/{menuName}/details")
    public ResponseEntity<List<CommonCodeDto>> getMenuDetailCodes(@PathVariable String menuName) {
        return ResponseEntity.ok(commonCodeService.findMenuDetailCodes(menuName));
    }

    // "기타" 항목 확인
    @GetMapping("/menu/{menuName}/has-etc")
    public ResponseEntity<Boolean> hasEtcOption(@PathVariable String menuName) {
        return ResponseEntity.ok(commonCodeService.hasEtcOption(menuName));
    }

    // 모든 공통코드 데이터 삭제
    @DeleteMapping("/clear-all")
    public ResponseEntity<String> clearAllCodes() {
        commonCodeService.clearAllCodes();
        return ResponseEntity.ok("모든 공통코드 데이터가 삭제되었습니다.");
    }
} 