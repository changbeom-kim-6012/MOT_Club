-- 과정별 세부소개자료 테이블 생성
CREATE TABLE IF NOT EXISTS course_materials (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id VARCHAR(10) NOT NULL COMMENT '과정 ID (예: 1, 2)',
    section_id VARCHAR(10) NOT NULL COMMENT '섹션 ID (예: I, II, III)',
    topic_id VARCHAR(10) NOT NULL COMMENT '토픽 ID (예: 1, 2, 3)',
    item_id VARCHAR(10) NOT NULL COMMENT '아이템 ID (예: 1, 2, 3, 4)',
    title VARCHAR(255) NOT NULL COMMENT '자료 제목',
    description TEXT COMMENT '자료 설명',
    file_name VARCHAR(255) COMMENT '원본 파일명',
    file_path VARCHAR(500) COMMENT '서버 저장 경로',
    file_size BIGINT COMMENT '파일 크기',
    uploaded_by VARCHAR(100) COMMENT '업로더',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_course_item (course_id, section_id, topic_id, item_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='과정별 세부소개자료'; 