package com.erns.mot.service;

import com.erns.mot.domain.Community;
import com.erns.mot.domain.CommonCode;
import com.erns.mot.dto.CommunityDto;
import com.erns.mot.repository.CommunityRepository;
import com.erns.mot.repository.CommonCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final CommonCodeRepository commonCodeRepository;

    @Autowired
    public CommunityService(CommunityRepository communityRepository, CommonCodeRepository commonCodeRepository) {
        this.communityRepository = communityRepository;
        this.commonCodeRepository = commonCodeRepository;
    }

    // 모든 커뮤니티 게시글 조회 (DTO로 변환)
    public List<CommunityDto> findAll() {
        List<Community> communities = communityRepository.findAll();
        return communities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ID로 커뮤니티 게시글 조회
    public CommunityDto findById(Long id) {
        Community community = communityRepository.findById(id).orElse(null);
        return community != null ? convertToDto(community) : null;
    }

    // 카테고리별 커뮤니티 게시글 조회
    public List<CommunityDto> findByCategoryId(Long categoryId) {
        List<Community> communities = communityRepository.findByCategoryId(categoryId);
        return communities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 제목 또는 내용으로 검색
    public List<CommunityDto> searchByTitleOrContent(String searchTerm) {
        List<Community> communities = communityRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(
                searchTerm, searchTerm);
        return communities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 커뮤니티 게시글 저장
    @Transactional
    public CommunityDto save(CommunityDto communityDto) {
        Community community = convertToEntity(communityDto);
        Community saved = communityRepository.save(community);
        return convertToDto(saved);
    }

    // 커뮤니티 게시글 수정
    @Transactional
    public CommunityDto update(Long id, CommunityDto communityDto) {
        Community existing = communityRepository.findById(id).orElse(null);
        if (existing == null) {
            return null;
        }

        // 카테고리 설정
        if (communityDto.getCategoryId() != null) {
            CommonCode category = commonCodeRepository.findById(communityDto.getCategoryId()).orElse(null);
            existing.setCategory(category);
        }

        existing.setTitle(communityDto.getTitle());
        existing.setContent(communityDto.getContent());
        existing.setAuthor(communityDto.getAuthor());

        Community updated = communityRepository.save(existing);
        return convertToDto(updated);
    }

    // 커뮤니티 게시글 삭제
    @Transactional
    public void delete(Long id) {
        communityRepository.deleteById(id);
    }

    // Entity를 DTO로 변환
    private CommunityDto convertToDto(Community community) {
        CommunityDto dto = new CommunityDto();
        dto.setId(community.getId());
        dto.setTitle(community.getTitle());
        dto.setContent(community.getContent());
        dto.setAuthor(community.getAuthor());
        dto.setCreatedAt(community.getCreatedAt());
        dto.setUpdatedAt(community.getUpdatedAt());
        
        if (community.getCategory() != null) {
            dto.setCategoryId(community.getCategory().getId());
            dto.setCategoryName(community.getCategory().getCodeName());
        }
        
        return dto;
    }

    // DTO를 Entity로 변환
    private Community convertToEntity(CommunityDto dto) {
        Community community = new Community();
        community.setTitle(dto.getTitle());
        community.setContent(dto.getContent());
        community.setAuthor(dto.getAuthor());
        
        if (dto.getCategoryId() != null) {
            CommonCode category = commonCodeRepository.findById(dto.getCategoryId()).orElse(null);
            community.setCategory(category);
        }
        
        return community;
    }
} 