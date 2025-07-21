package com.erns.mot.repository;

import com.erns.mot.domain.Opinion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpinionRepository extends JpaRepository<Opinion, Long> {
    // 필요시 커스텀 쿼리 추가 가능
} 