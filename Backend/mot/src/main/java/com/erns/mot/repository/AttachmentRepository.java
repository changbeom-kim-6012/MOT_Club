package com.erns.mot.repository;

import com.erns.mot.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByRefTableAndRefId(String refTable, Long refId);
    void deleteByRefTableAndRefId(String refTable, Long refId);
} 