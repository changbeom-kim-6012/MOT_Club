-- 각 메뉴별로 "기타" 항목 추가 스크립트
-- 이 스크립트는 기존 데이터베이스에 "기타" 옵션을 추가합니다.

-- Library 메뉴의 "기타" 항목 추가
INSERT INTO common_codes (menu_name, code_name, code_value, description, parent_id)
SELECT 'Library', '기타', 'ETC', '기타 자료출처', parent.id
FROM common_codes parent
WHERE parent.menu_name = 'Library' AND parent.parent_id IS NULL
AND NOT EXISTS (
    SELECT 1 FROM common_codes child 
    WHERE child.menu_name = 'Library' 
    AND child.code_name = '기타' 
    AND child.parent_id = parent.id
);

-- Q&A 메뉴의 "기타" 항목 추가
INSERT INTO common_codes (menu_name, code_name, code_value, description, parent_id)
SELECT 'Q&A', '기타', 'ETC', '기타 질문유형', parent.id
FROM common_codes parent
WHERE parent.menu_name = 'Q&A' AND parent.parent_id IS NULL
AND NOT EXISTS (
    SELECT 1 FROM common_codes child 
    WHERE child.menu_name = 'Q&A' 
    AND child.code_name = '기타' 
    AND child.parent_id = parent.id
);

-- Agora 메뉴의 "기타" 항목 추가
INSERT INTO common_codes (menu_name, code_name, code_value, description, parent_id)
SELECT 'Agora', '기타', 'ETC', '기타 기고분야', parent.id
FROM common_codes parent
WHERE parent.menu_name = 'Agora' AND parent.parent_id IS NULL
AND NOT EXISTS (
    SELECT 1 FROM common_codes child 
    WHERE child.menu_name = 'Agora' 
    AND child.code_name = '기타' 
    AND child.parent_id = parent.id
);

-- Learning 메뉴의 "기타" 항목 추가
INSERT INTO common_codes (menu_name, code_name, code_value, description, parent_id)
SELECT 'Learning', '기타', 'ETC', '기타 학습분야', parent.id
FROM common_codes parent
WHERE parent.menu_name = 'Learning' AND parent.parent_id IS NULL
AND NOT EXISTS (
    SELECT 1 FROM common_codes child 
    WHERE child.menu_name = 'Learning' 
    AND child.code_name = '기타' 
    AND child.parent_id = parent.id
);

-- Community 메뉴의 "기타" 항목 추가
INSERT INTO common_codes (menu_name, code_name, code_value, description, parent_id)
SELECT 'Community', '기타', 'ETC', '기타 커뮤니티 분야', parent.id
FROM common_codes parent
WHERE parent.menu_name = 'Community' AND parent.parent_id IS NULL
AND NOT EXISTS (
    SELECT 1 FROM common_codes child 
    WHERE child.menu_name = 'Community' 
    AND child.code_name = '기타' 
    AND child.parent_id = parent.id
);

-- 추가된 "기타" 항목 확인
SELECT 'Library' as menu_name, COUNT(*) as etc_count FROM common_codes 
WHERE menu_name = 'Library' AND code_name = '기타'
UNION ALL
SELECT 'Q&A' as menu_name, COUNT(*) as etc_count FROM common_codes 
WHERE menu_name = 'Q&A' AND code_name = '기타'
UNION ALL
SELECT 'Agora' as menu_name, COUNT(*) as etc_count FROM common_codes 
WHERE menu_name = 'Agora' AND code_name = '기타'
UNION ALL
SELECT 'Learning' as menu_name, COUNT(*) as etc_count FROM common_codes 
WHERE menu_name = 'Learning' AND code_name = '기타'
UNION ALL
SELECT 'Community' as menu_name, COUNT(*) as etc_count FROM common_codes 
WHERE menu_name = 'Community' AND code_name = '기타'; 