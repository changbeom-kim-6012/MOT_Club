package com.erns.mot.repository;

import com.erns.mot.domain.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
    // 카테고리 ID로 조회
    List<Community> findByCategoryId(Long categoryId);
    
    // 제목 또는 내용으로 검색 (대소문자 무시)
    List<Community> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);
} 