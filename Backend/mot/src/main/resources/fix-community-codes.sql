-- Community 메뉴의 하위 코드들 menu_name 수정 스크립트
-- parent_id가 25번인 코드들의 menu_name을 'Library'에서 'Community'로 변경

-- 수정 전 상태 확인
SELECT '수정 전 상태' as status;
SELECT id, parent_id, menu_name, code_name, code_value 
FROM common_codes 
WHERE parent_id = 25 
ORDER BY id;

-- menu_name 수정 실행
UPDATE common_codes 
SET menu_name = 'Community' 
WHERE parent_id = 25 AND menu_name = 'Library';

-- 수정 후 상태 확인
SELECT '수정 후 상태' as status;
SELECT id, parent_id, menu_name, code_name, code_value 
FROM common_codes 
WHERE parent_id = 25 
ORDER BY id;

-- 전체 Community 메뉴 계층 구조 확인
SELECT 'Community 메뉴 전체 계층 구조' as info;
SELECT 
    c1.id as level1_id,
    c1.code_name as level1_name,
    c1.menu_name as level1_menu,
    c2.id as level2_id,
    c2.code_name as level2_name,
    c2.menu_name as level2_menu,
    c3.id as level3_id,
    c3.code_name as level3_name,
    c3.menu_name as level3_menu
FROM common_codes c1
LEFT JOIN common_codes c2 ON c2.parent_id = c1.id
LEFT JOIN common_codes c3 ON c3.parent_id = c2.id
WHERE c1.menu_name = 'Community' AND c1.parent_id IS NULL
ORDER BY c1.id, c2.id, c3.id;

-- 수정된 레코드 수 확인
SELECT COUNT(*) as updated_count 
FROM common_codes 
WHERE parent_id = 25 AND menu_name = 'Community'; 