package com.erns.mot.repository;

import com.erns.mot.domain.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpertRepository extends JpaRepository<Expert, Long> {
    
    // 이름으로 검색
    List<Expert> findByNameContainingIgnoreCase(String name);
    
    // 이메일로 검색
    List<Expert> findByEmailContainingIgnoreCase(String email);
    
    // 조직으로 검색
    List<Expert> findByOrganizationContainingIgnoreCase(String organization);
    
    // 전문분야로 검색
    List<Expert> findByFieldContainingIgnoreCase(String field);
    
    // 상태로 필터링
    List<Expert> findByStatus(String status);
    
    // 복합 검색
    @Query("SELECT e FROM Expert e WHERE " +
           "(:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:email IS NULL OR LOWER(e.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:organization IS NULL OR LOWER(e.organization) LIKE LOWER(CONCAT('%', :organization, '%'))) AND " +
           "(:field IS NULL OR LOWER(e.field) LIKE LOWER(CONCAT('%', :field, '%'))) AND " +
           "(:status IS NULL OR e.status = :status)")
    List<Expert> findBySearchCriteria(
        @Param("name") String name,
        @Param("email") String email,
        @Param("organization") String organization,
        @Param("field") String field,
        @Param("status") String status
    );
    
    // 활성 상태의 전문가만 조회
    List<Expert> findByStatusOrderByNameAsc(String status);
} 