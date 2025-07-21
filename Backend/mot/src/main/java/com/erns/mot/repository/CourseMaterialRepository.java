package com.erns.mot.repository;

import com.erns.mot.domain.CourseMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseMaterialRepository extends JpaRepository<CourseMaterial, Long> {
    
    // 특정 과정의 특정 아이템에 대한 자료 조회
    @Query("SELECT cm FROM CourseMaterial cm WHERE cm.courseId = :courseId AND cm.sectionId = :sectionId AND cm.topicId = :topicId AND cm.itemId = :itemId ORDER BY cm.createdAt DESC")
    List<CourseMaterial> findByCourseAndItem(@Param("courseId") String courseId, 
                                           @Param("sectionId") String sectionId, 
                                           @Param("topicId") String topicId, 
                                           @Param("itemId") String itemId);
    
    // 특정 과정의 모든 자료 조회
    @Query("SELECT cm FROM CourseMaterial cm WHERE cm.courseId = :courseId ORDER BY cm.sectionId, cm.topicId, cm.itemId, cm.createdAt DESC")
    List<CourseMaterial> findByCourseId(@Param("courseId") String courseId);
} 