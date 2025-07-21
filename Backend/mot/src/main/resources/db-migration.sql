-- Q&A 테이블 구조 변경 마이그레이션 스크립트
-- 기존 authorId, authorName 컬럼을 제거하고 authorEmail, isPublic 컬럼 추가

-- 1. 기존 컬럼 제거 (데이터 백업 후 실행)
ALTER TABLE questions DROP COLUMN IF EXISTS author_id;
ALTER TABLE questions DROP COLUMN IF EXISTS author_name;

-- 2. 새로운 컬럼 추가
ALTER TABLE questions ADD COLUMN IF NOT EXISTS author_email VARCHAR(255);
ALTER TABLE questions ADD COLUMN IF NOT EXISTS is_public BOOLEAN DEFAULT TRUE;

-- 3. 기존 데이터가 있다면 기본값 설정 (필요시 수정)
-- UPDATE questions SET is_public = TRUE WHERE is_public IS NULL;

-- 4. 인덱스 추가 (성능 향상을 위해)
CREATE INDEX IF NOT EXISTS idx_questions_author_email ON questions(author_email);
CREATE INDEX IF NOT EXISTS idx_questions_is_public ON questions(is_public);
CREATE INDEX IF NOT EXISTS idx_questions_created_at ON questions(created_at);

-- 5. 제약조건 추가 (필요시)
-- ALTER TABLE questions ALTER COLUMN author_email SET NOT NULL;

-- Common Codes Table에 sort_order 컬럼 추가
ALTER TABLE common_codes ADD COLUMN IF NOT EXISTS sort_order INTEGER;

-- Common Codes Table에 sort_order 인덱스 추가 (정렬 성능 향상)
CREATE INDEX IF NOT EXISTS idx_common_codes_sort_order ON common_codes(sort_order);

-- Existing tables (if any) will be here.

-- Common Codes Table with hierarchy
-- Note: The parent_id column needs to be added to your existing common_codes table.
-- Example alter statement (if needed):
-- ALTER TABLE common_codes ADD COLUMN parent_id BIGINT;
-- ALTER TABLE common_codes ADD CONSTRAINT fk_parent FOREIGN KEY (parent_id) REFERENCES common_codes (id);


-- Q&A Tables based on the screen design
CREATE TABLE IF NOT EXISTS questions (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author_email VARCHAR(255),
    category1 VARCHAR(100), -- Managed by Common Codes
    category2 VARCHAR(100), -- Managed by Common Codes
    view_count INT DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS answers (
    id BIGSERIAL PRIMARY KEY,
    question_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    author_name VARCHAR(255),
    author_email VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_question FOREIGN KEY(question_id) REFERENCES questions(id) ON DELETE CASCADE
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_answers_question_id ON answers(question_id);

-- Opinion 테이블에 category 컬럼 추가
ALTER TABLE opinions ADD COLUMN IF NOT EXISTS category VARCHAR(100);

-- Opinion 테이블 인덱스 추가
CREATE INDEX IF NOT EXISTS idx_opinions_category ON opinions(category);
CREATE INDEX IF NOT EXISTS idx_opinions_status ON opinions(status);
CREATE INDEX IF NOT EXISTS idx_opinions_created_at ON opinions(created_at);

-- Opinion 테이블에 fullText 컬럼 추가
ALTER TABLE opinions ADD COLUMN IF NOT EXISTS full_text TEXT;

-- Opinion 테이블 fullText 인덱스 추가 (검색 성능 향상)
CREATE INDEX IF NOT EXISTS idx_opinions_full_text ON opinions USING gin(to_tsvector('english', full_text));

-- 이메일 인증코드 테이블 제거 (더 이상 사용하지 않음)
-- DROP TABLE IF EXISTS auth_codes;
-- DROP INDEX IF EXISTS idx_auth_codes_expire_at; 