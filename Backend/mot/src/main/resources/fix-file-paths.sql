-- Library 테이블의 잘못된 filePaths를 수정
UPDATE library 
SET file_paths = '3ccb17ca-e1e5-479a-ae00-c88b0dd8c2d0_Ⅰ- B1. 환경분석과 Scenario Planning(구성).pdf'
WHERE id = 1; 