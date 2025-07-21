-- 이메일 인증 관련 테이블 및 인덱스 삭제 스크립트
-- 이 스크립트는 기존 데이터베이스에서 auth_codes 테이블을 완전히 제거합니다.

-- 인덱스 삭제
DROP INDEX IF EXISTS idx_auth_codes_expire_at;

-- 테이블 삭제
DROP TABLE IF EXISTS auth_codes;

-- 삭제 확인
SELECT 'auth_codes 테이블이 성공적으로 삭제되었습니다.' as result; 