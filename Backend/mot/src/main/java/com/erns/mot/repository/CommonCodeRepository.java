package com.erns.mot.repository;

import com.erns.mot.domain.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommonCodeRepository extends JpaRepository<CommonCode, Long> {

    // Q&A 카테고리 조회 (Library 패턴과 동일)
    List<CommonCode> findByParentCodeName(String parentCodeName);
} 