package com.erns.mot.service;

import com.erns.mot.domain.CommonCode;
import com.erns.mot.repository.CommonCodeRepository;
import com.erns.mot.dto.CommonCodeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommonCodeService {

    private final CommonCodeRepository commonCodeRepository;

    public List<CommonCode> findAll() {
        return commonCodeRepository.findAll();
    }

    @Transactional
    public CommonCode save(CommonCodeDto dto) {
        CommonCode code = new CommonCode();
        code.setMenuName(dto.getMenuName());
        code.setCodeName(dto.getCodeName());
        code.setCodeValue(dto.getCodeValue());
        code.setDescription(dto.getDescription());
        
        // 3단계 코드인 경우 순서 설정
        if (dto.getParentId() != null) {
            CommonCode parent = commonCodeRepository.findById(dto.getParentId())
                .orElseThrow(() -> new IllegalArgumentException("상위 코드가 존재하지 않습니다."));
            code.setParent(parent);
            
            // 3단계 코드인지 확인 (2단계 코드의 하위)
            if (parent.getParent() != null) {
                // 현재 2단계 코드의 하위 코드 중 최대 순서 + 1
                Integer maxSortOrder = commonCodeRepository.findAll().stream()
                    .filter(child -> child.getParent() != null && child.getParent().getId().equals(parent.getId()))
                    .mapToInt(child -> child.getSortOrder() != null ? child.getSortOrder() : 0)
                    .max()
                    .orElse(0);
                code.setSortOrder(maxSortOrder + 1);
            }
        }
        
        return commonCodeRepository.save(code);
    }

    @Transactional
    public void createDummyData() {
        // 기존 데이터가 없을 때만 더미 데이터 생성
        if (commonCodeRepository.count() == 0) {
            // Library 3단계 계층 구조 생성
            // 1단계: Library (메뉴 레벨)
            CommonCode libraryMaster = new CommonCode();
            libraryMaster.setMenuName("Library");
            libraryMaster.setCodeName("Library");
            libraryMaster.setCodeValue("LIBRARY");
            libraryMaster.setDescription("자료 관리");
            libraryMaster = commonCodeRepository.save(libraryMaster);

            // 2단계: 자료출처 (하위 레벨)
            CommonCode librarySource = new CommonCode();
            librarySource.setMenuName("Library");
            librarySource.setCodeName("자료출처");
            librarySource.setCodeValue("SOURCE");
            librarySource.setDescription("자료의 출처 분류");
            librarySource.setParent(libraryMaster);
            librarySource = commonCodeRepository.save(librarySource);

            // 3단계: 세부하위 레벨 (기술, 경영 등)
            CommonCode technical = new CommonCode();
            technical.setMenuName("Library");
            technical.setCodeName("기술");
            technical.setCodeValue("TECHNICAL");
            technical.setDescription("기술 관련 자료");
            technical.setSortOrder(1);
            technical.setParent(librarySource);
            commonCodeRepository.save(technical);

            CommonCode management = new CommonCode();
            management.setMenuName("Library");
            management.setCodeName("경영");
            management.setCodeValue("MANAGEMENT");
            management.setDescription("경영 관련 자료");
            management.setSortOrder(2);
            management.setParent(librarySource);
            commonCodeRepository.save(management);

            CommonCode research = new CommonCode();
            research.setMenuName("Library");
            research.setCodeName("연구");
            research.setCodeValue("RESEARCH");
            research.setDescription("연구 관련 자료");
            research.setSortOrder(3);
            research.setParent(librarySource);
            commonCodeRepository.save(research);

            CommonCode etc = new CommonCode();
            etc.setMenuName("Library");
            etc.setCodeName("기타");
            etc.setCodeValue("ETC");
            etc.setDescription("기타 자료출처");
            etc.setSortOrder(4);
            etc.setParent(librarySource);
            commonCodeRepository.save(etc);

            // Agora 3단계 계층 구조 생성
            // 1단계: Agora (메뉴 레벨)
            CommonCode agoraMaster = new CommonCode();
            agoraMaster.setMenuName("Agora");
            agoraMaster.setCodeName("Agora");
            agoraMaster.setCodeValue("AGORA");
            agoraMaster.setDescription("기고 관리");
            agoraMaster = commonCodeRepository.save(agoraMaster);

            // 2단계: 기고분야 (하위 레벨)
            CommonCode agoraField = new CommonCode();
            agoraField.setMenuName("Agora");
            agoraField.setCodeName("기고분야");
            agoraField.setCodeValue("FIELD");
            agoraField.setDescription("기고의 분야 분류");
            agoraField.setParent(agoraMaster);
            agoraField = commonCodeRepository.save(agoraField);

            // 3단계: 세부하위 레벨
            CommonCode agoraTechnical = new CommonCode();
            agoraTechnical.setMenuName("Agora");
            agoraTechnical.setCodeName("기술");
            agoraTechnical.setCodeValue("TECHNICAL");
            agoraTechnical.setDescription("기술 관련 기고");
            agoraTechnical.setSortOrder(1);
            agoraTechnical.setParent(agoraField);
            commonCodeRepository.save(agoraTechnical);

            CommonCode agoraManagement = new CommonCode();
            agoraManagement.setMenuName("Agora");
            agoraManagement.setCodeName("경영");
            agoraManagement.setCodeValue("MANAGEMENT");
            agoraManagement.setDescription("경영 관련 기고");
            agoraManagement.setSortOrder(2);
            agoraManagement.setParent(agoraField);
            commonCodeRepository.save(agoraManagement);

            CommonCode agoraEtc = new CommonCode();
            agoraEtc.setMenuName("Agora");
            agoraEtc.setCodeName("기타");
            agoraEtc.setCodeValue("ETC");
            agoraEtc.setDescription("기타 기고분야");
            agoraEtc.setSortOrder(3);
            agoraEtc.setParent(agoraField);
            commonCodeRepository.save(agoraEtc);

            // Community 3단계 계층 구조 생성
            // 1단계: Community (메뉴 레벨)
            CommonCode communityMaster = new CommonCode();
            communityMaster.setMenuName("Community");
            communityMaster.setCodeName("Community");
            communityMaster.setCodeValue("COMMUNITY");
            communityMaster.setDescription("커뮤니티 관리");
            communityMaster = commonCodeRepository.save(communityMaster);

            // 2단계: 게시판유형 (하위 레벨)
            CommonCode communityType = new CommonCode();
            communityType.setMenuName("Community");
            communityType.setCodeName("게시판유형");
            communityType.setCodeValue("TYPE");
            communityType.setDescription("게시판의 유형 분류");
            communityType.setParent(communityMaster);
            communityType = commonCodeRepository.save(communityType);

            // 3단계: 세부하위 레벨
            CommonCode communityNotice = new CommonCode();
            communityNotice.setMenuName("Community");
            communityNotice.setCodeName("공지사항");
            communityNotice.setCodeValue("NOTICE");
            communityNotice.setDescription("공지사항 게시판");
            communityNotice.setSortOrder(1);
            communityNotice.setParent(communityType);
            commonCodeRepository.save(communityNotice);

            CommonCode communityNews = new CommonCode();
            communityNews.setMenuName("Community");
            communityNews.setCodeName("뉴스");
            communityNews.setCodeValue("NEWS");
            communityNews.setDescription("뉴스 게시판");
            communityNews.setSortOrder(2);
            communityNews.setParent(communityType);
            commonCodeRepository.save(communityNews);

            CommonCode communityEvent = new CommonCode();
            communityEvent.setMenuName("Community");
            communityEvent.setCodeName("이벤트");
            communityEvent.setCodeValue("EVENT");
            communityEvent.setDescription("이벤트 게시판");
            communityEvent.setSortOrder(3);
            communityEvent.setParent(communityType);
            commonCodeRepository.save(communityEvent);

            CommonCode communityEtc = new CommonCode();
            communityEtc.setMenuName("Community");
            communityEtc.setCodeName("기타");
            communityEtc.setCodeValue("ETC");
            communityEtc.setDescription("기타 게시판유형");
            communityEtc.setSortOrder(4);
            communityEtc.setParent(communityType);
            commonCodeRepository.save(communityEtc);
        }
    }

    @Transactional
    public CommonCode update(Long id, CommonCodeDto dto) {
        CommonCode code = commonCodeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 코드가 존재하지 않습니다. id=" + id));
        code.setMenuName(dto.getMenuName());
        code.setCodeName(dto.getCodeName());
        code.setCodeValue(dto.getCodeValue());
        code.setDescription(dto.getDescription());
        code.setSortOrder(dto.getSortOrder());
        if (dto.getParentId() != null) {
            CommonCode parent = commonCodeRepository.findById(dto.getParentId())
                .orElseThrow(() -> new IllegalArgumentException("상위 코드가 존재하지 않습니다."));
            code.setParent(parent);
        } else {
            code.setParent(null);
        }
        return commonCodeRepository.save(code);
    }

    @Transactional
    public void delete(Long id) {
        commonCodeRepository.deleteById(id);
    }

    // 엔티티를 DTO로 변환 (children 계층 포함)
    public CommonCodeDto toDto(CommonCode code) {
        CommonCodeDto dto = new CommonCodeDto();
        dto.setId(code.getId());
        dto.setMenuName(code.getMenuName());
        dto.setCodeName(code.getCodeName());
        dto.setCodeValue(code.getCodeValue());
        dto.setDescription(code.getDescription());
        dto.setSortOrder(code.getSortOrder());
        dto.setParentId(code.getParent() != null ? code.getParent().getId() : null);
        
        // children을 명시적으로 로드하여 계층 구조 생성
        List<CommonCode> children = commonCodeRepository.findAll().stream()
            .filter(child -> child.getParent() != null && child.getParent().getId().equals(code.getId()))
            .sorted((a, b) -> {
                // 3단계 코드인 경우 sortOrder로 정렬, 그 외에는 codeName으로 정렬
                if (a.getParent() != null && a.getParent().getParent() != null) {
                    // 3단계 코드
                    Integer sortA = a.getSortOrder() != null ? a.getSortOrder() : 0;
                    Integer sortB = b.getSortOrder() != null ? b.getSortOrder() : 0;
                    return sortA.compareTo(sortB);
                } else {
                    // 1단계, 2단계 코드
                    return a.getCodeName().compareTo(b.getCodeName());
                }
            })
            .collect(Collectors.toList());
            
        if (!children.isEmpty()) {
            dto.setChildren(children.stream().map(this::toDto).collect(Collectors.toList()));
        } else {
            dto.setChildren(null);
        }
        return dto;
    }

    // 전체 계층구조를 반환 (1단계 코드와 그 하위 코드들 모두 포함)
    public List<CommonCodeDto> findAllDto() {
        return commonCodeRepository.findAll().stream()
            .filter(code -> code.getParent() == null)
            .sorted((a, b) -> a.getCodeName().compareTo(b.getCodeName()))
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    // Library 1단계 코드(분류코드)만 조회
    public List<CommonCodeDto> findLibraryMasterCodes() {
        return commonCodeRepository.findAll().stream()
            .filter(code -> code.getParent() == null && "Library".equals(code.getMenuName()))
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    // Library 자료출처(1단계)의 하위 코드(2단계)만 조회
    public List<CommonCodeDto> findLibraryDetailCodes() {
        // 1단계: menuName=Library, parent=null 인 코드(자료출처) 찾기
        CommonCode master = commonCodeRepository.findAll().stream()
            .filter(code -> code.getParent() == null && "Library".equals(code.getMenuName()))
            .findFirst().orElse(null);
        if (master == null) return List.of();
        // 2단계: parent=master 인 코드만 반환
        return commonCodeRepository.findAll().stream()
            .filter(code -> code.getParent() != null && code.getParent().getId().equals(master.getId()))
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    // parentId에 해당하는 하위 코드만 반환
    public List<CommonCodeDto> findByParentIdDto(Long parentId) {
        return commonCodeRepository.findAll().stream()
            .filter(code -> code.getParent() != null && code.getParent().getId().equals(parentId))
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    // 통합된 메뉴별 하위 코드 조회 메서드 (3단계 계층 구조에서 3단계 코드 반환)
    public List<CommonCodeDto> findMenuDetailCodes(String menuName) {
        // 1단계: menuName으로 마스터 코드 찾기
        CommonCode master = commonCodeRepository.findAll().stream()
            .filter(code -> code.getParent() == null && menuName.equals(code.getMenuName()))
            .findFirst().orElse(null);
        if (master == null) return List.of();
        
        // 2단계: 마스터 코드의 하위 코드(2단계) 찾기
        List<CommonCode> secondLevel = commonCodeRepository.findAll().stream()
            .filter(code -> code.getParent() != null && code.getParent().getId().equals(master.getId()))
            .collect(Collectors.toList());
        
        if (secondLevel.isEmpty()) return List.of();
        
        // 3단계: 2단계 코드들의 하위 코드들(3단계) 반환 - sortOrder로 정렬
        return commonCodeRepository.findAll().stream()
            .filter(code -> code.getParent() != null && 
                secondLevel.stream().anyMatch(second -> second.getId().equals(code.getParent().getId())))
            .sorted((a, b) -> {
                Integer sortA = a.getSortOrder() != null ? a.getSortOrder() : 0;
                Integer sortB = b.getSortOrder() != null ? b.getSortOrder() : 0;
                return sortA.compareTo(sortB);
            })
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    // "기타" 항목 확인 메서드
    public boolean hasEtcOption(String menuName) {
        List<CommonCodeDto> codes = findMenuDetailCodes(menuName);
        return codes.stream().anyMatch(code -> "기타".equals(code.getCodeName()));
    }

    // Agora 하위 카테고리만 조회 (기존 메서드 유지)
    public List<CommonCodeDto> findAgoraDetailCodes() {
        return findMenuDetailCodes("Agora");
    }

    // 모든 공통코드 데이터 삭제
    @Transactional
    public void clearAllCodes() {
        commonCodeRepository.deleteAll();
    }
} 