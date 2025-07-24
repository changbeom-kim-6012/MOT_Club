-- Library 테이블 데이터 삭제 스크립트
-- 기존 Library 데이터를 모두 삭제하고 새로 테스트할 수 있도록 합니다.

-- 1. Library 테이블의 모든 데이터 삭제
DELETE FROM library;

-- 2. 시퀀스 리셋 (PostgreSQL)
ALTER SEQUENCE library_id_seq RESTART WITH 1;

-- 3. 삭제 확인
SELECT COUNT(*) as remaining_count FROM library;

-- 4. 업로드된 파일들도 삭제 (선택사항)
-- 실제 파일 시스템에서 uploads/library 폴더의 파일들을 수동으로 삭제하거나
-- 백엔드 애플리케이션에서 파일 삭제 기능을 구현하여 사용 