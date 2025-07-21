-- Community 테이블 구조 정의
-- 이 스크립트는 Community 엔티티에 해당하는 테이블 구조를 정의합니다.

CREATE TABLE IF NOT EXISTS community (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    author VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_community_category FOREIGN KEY (category_id) REFERENCES common_codes(id)
);

-- 인덱스 생성 (성능 향상을 위해)
CREATE INDEX IF NOT EXISTS idx_community_category_id ON community(category_id);
CREATE INDEX IF NOT EXISTS idx_community_created_at ON community(created_at);
CREATE INDEX IF NOT EXISTS idx_community_author ON community(author);

-- 기존 데이터 확인
SELECT COUNT(*) as total_count FROM community;

-- Community 데이터와 카테고리 정보 조인하여 확인
SELECT 
    c.id,
    c.title,
    c.author,
    c.created_at,
    cc.code_name as category_name,
    cc.menu_name
FROM community c
LEFT JOIN common_codes cc ON c.category_id = cc.id
ORDER BY c.created_at DESC; 