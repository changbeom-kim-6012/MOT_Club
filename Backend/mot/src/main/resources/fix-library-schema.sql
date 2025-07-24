-- Library 테이블 스키마 정리 및 파일 경로 수정
-- 1. 불필요한 컬럼 제거 (filenames, filepaths)
ALTER TABLE library DROP COLUMN IF EXISTS filenames;
ALTER TABLE library DROP COLUMN IF EXISTS filepaths;

-- 2. file_paths에서 파일명만 추출하여 저장
-- Windows 경로에서 파일명만 추출하는 함수
CREATE OR REPLACE FUNCTION extract_filename(file_path TEXT)
RETURNS TEXT AS $$
BEGIN
    -- Windows 경로 구분자 처리
    IF file_path LIKE '%\%' THEN
        RETURN substring(file_path from '.*\\([^\\]+)$');
    -- Unix 경로 구분자 처리
    ELSIF file_path LIKE '%/%' THEN
        RETURN substring(file_path from '.*/([^/]+)$');
    -- 이미 파일명만 있는 경우
    ELSE
        RETURN file_path;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- 3. 기존 file_paths를 파일명만으로 업데이트
UPDATE library 
SET file_paths = extract_filename(file_paths)
WHERE file_paths IS NOT NULL AND file_paths LIKE '%\%';

-- 4. 함수 삭제 (더 이상 필요 없음)
DROP FUNCTION IF EXISTS extract_filename(TEXT);

-- 5. 결과 확인을 위한 쿼리
-- SELECT id, file_names, file_paths FROM library WHERE file_paths IS NOT NULL; 