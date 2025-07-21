-- 공통코드 데이터 초기화 및 3단계 계층 구조 재생성 스크립트
-- 이 스크립트는 기존 공통코드 데이터를 모두 삭제하고 새로운 구조로 재생성합니다.

-- 1. 기존 공통코드 데이터 모두 삭제 (외래키 제약조건 때문에 순서 중요)
DELETE FROM common_codes WHERE parent_id IS NOT NULL;
DELETE FROM common_codes WHERE parent_id IS NULL;

-- 2. 시퀀스 리셋 (PostgreSQL)
ALTER SEQUENCE common_codes_id_seq RESTART WITH 1;

-- 3. 새로운 3단계 계층 구조 데이터 삽입

-- Library 3단계 계층 구조
-- 1단계: Library (메뉴 레벨)
INSERT INTO common_codes (menu_name, code_name, code_value, description, parent_id) 
VALUES ('Library', 'Library', 'LIBRARY', '자료 관리', NULL);

-- 2단계: 자료출처 (하위 레벨)
INSERT INTO common_codes (menu_name, code_name, code_value, description, parent_id)
SELECT 'Library', '자료출처', 'SOURCE', '자료의 출처 분류', id
FROM common_codes 
WHERE menu_name = 'Library' AND code_name = 'Library' AND parent_id IS NULL;

-- 3단계: 세부하위 레벨 (기술, 경영 등)
INSERT INTO common_codes (menu_name, code_name, code_value, description, parent_id)
SELECT 'Library', '기술', 'TECHNICAL', '기술 관련 자료', id
FROM common_codes 
WHERE menu_name = 'Library' AND code_name = '자료출처';

INSERT INTO common_codes (menu_name, code_name, code_value, description, parent_id)
SELECT 'Library', '경영', 'MANAGEMENT', '경영 관련 자료', id
FROM common_codes 
WHERE menu_name = 'Library' AND code_name = '자료출처';

INSERT INTO common_codes (menu_name, code_name, code_value, description, parent_id)
SELECT 'Library', '연구', 'RESEARCH', '연구 관련 자료', id
FROM common_codes 
WHERE menu_name = 'Library' AND code_name = '자료출처';

INSERT INTO common_codes (menu_name, code_name, code_value, description, parent_id)
SELECT 'Library', '기타', 'ETC', '기타 자료출처', id
FROM common_codes 
WHERE menu_name = 'Library' AND code_name = '자료출처';

-- Q&A 3단계 계층 구조
-- 1단계: Q&A (메뉴 레벨)
INSERT INTO common_codes (menu_name, code_name, code_value, description, parent_id) 
VALUES ('Q&A', 'Q&A', 'QNA', '질문과 답변', NULL);

-- 2단계: 질문유형 (하위 레벨)
INSERT INTO common_codes (menu_name, code_name, code_value, description, parent_id)
SELECT 'Q&A', '질문유형', 'TYPE', '질문의 유형 분류', id
FROM common_codes 
WHERE menu_name = 'Q&A' AND code_name = 'Q&A' AND parent_id IS NULL;

-- 3단계: 세부하위 레벨
INSERT INTO common_codes (menu_name, code_name, code_value, description, parent_id)
SELECT 'Q&A', '기술', 'TECHNICAL', '기술 관련 질문', id
FROM common_codes 
WHERE menu_name = 'Q&A' AND code_name = '질문유형';

INSERT INTO common_codes (menu_name, code_name, code_value, description, parent_id)
SELECT 'Q&A', '경영', 'MANAGEMENT', '경영 관련 질문', id
FROM common_codes 
WHERE menu_name = 'Q&A' AND code_name = '질문유형';

INSERT INTO common_codes (menu_name, code_name, code_value, description, parent_id)
SELECT 'Q&A', '기타', 'ETC', '기타 질문유형', id
FROM common_codes 
WHERE menu_name = 'Q&A' AND code_name = '질문유형';

-- Agora 3단계 계층 구조
-- 1단계: Agora (메뉴 레벨)
INSERT INTO common_codes (menu_name, code_name, code_value, description, parent_id) 
VALUES ('Agora', 'Agora', 'AGORA', '기고 관리', NULL);

-- 2단계: 기고분야 (하위 레벨)
INSERT INTO common_codes (menu_name, code_name, code_value, description, parent_id)
SELECT 'Agora', '기고분야', 'FIELD', '기고의 분야 분류', id
FROM common_codes 
WHERE menu_name = 'Agora' AND code_name = 'Agora' AND parent_id IS NULL;

-- 3단계: 세부하위 레벨
INSERT INTO common_codes (menu_name, code_name, code_value, description, parent_id)
SELECT 'Agora', '기술', 'TECHNICAL', '기술 관련 기고', id
FROM common_codes 
WHERE menu_name = 'Agora' AND code_name = '기고분야';

INSERT INTO common_codes (menu_name, code_name, code_value, description, parent_id)
SELECT 'Agora', '경영', 'MANAGEMENT', '경영 관련 기고', id
FROM common_codes 
WHERE menu_name = 'Agora' AND code_name = '기고분야';

INSERT INTO common_codes (menu_name, code_name, code_value, description, parent_id)
SELECT 'Agora', '기타', 'ETC', '기타 기고분야', id
FROM common_codes 
WHERE menu_name = 'Agora' AND code_name = '기고분야';

-- 생성된 데이터 확인
SELECT 'Library' as menu, COUNT(*) as count FROM common_codes WHERE menu_name = 'Library'
UNION ALL
SELECT 'Q&A' as menu, COUNT(*) as count FROM common_codes WHERE menu_name = 'Q&A'
UNION ALL
SELECT 'Agora' as menu, COUNT(*) as count FROM common_codes WHERE menu_name = 'Agora';

-- 계층 구조 확인
SELECT 
    c1.code_name as level1,
    c2.code_name as level2,
    c3.code_name as level3
FROM common_codes c1
LEFT JOIN common_codes c2 ON c2.parent_id = c1.id
LEFT JOIN common_codes c3 ON c3.parent_id = c2.id
WHERE c1.parent_id IS NULL
ORDER BY c1.menu_name, c1.code_name, c2.code_name, c3.code_name; 