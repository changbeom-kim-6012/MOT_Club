-- Library 테이블 파일 컬럼 마이그레이션
-- 기존 fileName, filePath 컬럼을 fileNames, filePaths로 변경

-- 1. 새로운 컬럼 추가
ALTER TABLE library ADD COLUMN fileNames TEXT;
ALTER TABLE library ADD COLUMN filePaths TEXT;

-- 2. 기존 데이터 마이그레이션 (기존 fileName, filePath 값을 새로운 컬럼으로 복사)
UPDATE library SET fileNames = fileName WHERE fileName IS NOT NULL;
UPDATE library SET filePaths = filePath WHERE filePath IS NOT NULL;

-- 3. 기존 컬럼 삭제
ALTER TABLE library DROP COLUMN fileName;
ALTER TABLE library DROP COLUMN filePath; 