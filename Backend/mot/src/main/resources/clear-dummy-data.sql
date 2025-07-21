-- 더미 데이터 삭제 스크립트
-- 이 스크립트는 기존 공통코드 데이터를 모두 삭제합니다.

-- 1. 기존 공통코드 데이터 모두 삭제 (외래키 제약조건 때문에 순서 중요)
DELETE FROM common_codes WHERE parent_id IS NOT NULL;
DELETE FROM common_codes WHERE parent_id IS NULL;

-- 2. 시퀀스 리셋 (PostgreSQL)
ALTER SEQUENCE common_codes_id_seq RESTART WITH 1;

-- 3. 삭제 확인
SELECT '삭제 완료' as status, COUNT(*) as remaining_count FROM common_codes; 