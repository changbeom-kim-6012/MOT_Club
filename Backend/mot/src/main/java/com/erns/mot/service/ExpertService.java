package com.erns.mot.service;

import com.erns.mot.domain.Expert;
import com.erns.mot.dto.ExpertCreateDto;
import com.erns.mot.dto.ExpertDto;
import com.erns.mot.dto.ExpertUpdateDto;
import com.erns.mot.repository.ExpertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpertService {
    private final ExpertRepository expertRepository;

    @Autowired
    public ExpertService(ExpertRepository expertRepository) {
        this.expertRepository = expertRepository;
    }

    // DTO를 엔티티로 변환
    private ExpertDto convertToDto(Expert expert) {
        return new ExpertDto(
            expert.getId(),
            expert.getName(),
            expert.getEmail(),
            expert.getPhone(),
            expert.getOrganization(),
            expert.getPosition(),
            expert.getEducation(),
            expert.getCareer(),
            expert.getField(),
            expert.getStatus(),
            expert.getCreatedAt(),
            expert.getLastLogin(),
            expert.getUpdatedAt()
        );
    }

    // 엔티티를 DTO로 변환
    private Expert convertToEntity(ExpertCreateDto dto) {
        Expert expert = new Expert();
        expert.setName(dto.getName());
        expert.setEmail(dto.getEmail());
        expert.setPhone(dto.getPhone());
        expert.setOrganization(dto.getOrganization());
        expert.setPosition(dto.getPosition());
        expert.setEducation(dto.getEducation());
        expert.setCareer(dto.getCareer());
        expert.setField(dto.getField());
        expert.setStatus(dto.getStatus());
        return expert;
    }

    // 전문가 생성
    public ExpertDto createExpert(ExpertCreateDto expertDto) {
        // 이메일 중복 체크
        if (expertRepository.findByEmailContainingIgnoreCase(expertDto.getEmail()).size() > 0) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        
        Expert expert = convertToEntity(expertDto);
        Expert savedExpert = expertRepository.save(expert);
        return convertToDto(savedExpert);
    }

    // 전문가 수정
    public ExpertDto updateExpert(Long id, ExpertUpdateDto expertDto) {
        Optional<Expert> optionalExpert = expertRepository.findById(id);
        if (optionalExpert.isEmpty()) {
            throw new RuntimeException("전문가를 찾을 수 없습니다.");
        }
        
        Expert expert = optionalExpert.get();
        
        // 이메일 변경 시 중복 체크
        if (!expert.getEmail().equals(expertDto.getEmail())) {
            if (expertRepository.findByEmailContainingIgnoreCase(expertDto.getEmail()).size() > 0) {
                throw new RuntimeException("이미 존재하는 이메일입니다.");
            }
        }
        
        expert.setName(expertDto.getName());
        expert.setEmail(expertDto.getEmail());
        expert.setPhone(expertDto.getPhone());
        expert.setOrganization(expertDto.getOrganization());
        expert.setPosition(expertDto.getPosition());
        expert.setEducation(expertDto.getEducation());
        expert.setCareer(expertDto.getCareer());
        expert.setField(expertDto.getField());
        expert.setStatus(expertDto.getStatus());
        expert.setUpdatedAt(LocalDateTime.now());
        
        Expert updatedExpert = expertRepository.save(expert);
        return convertToDto(updatedExpert);
    }

    // 전문가 삭제
    public void deleteExpert(Long id) {
        if (!expertRepository.existsById(id)) {
            throw new RuntimeException("전문가를 찾을 수 없습니다.");
        }
        expertRepository.deleteById(id);
    }

    // 전문가 조회 (단일)
    public ExpertDto getExpert(Long id) {
        Optional<Expert> expert = expertRepository.findById(id);
        if (expert.isEmpty()) {
            throw new RuntimeException("전문가를 찾을 수 없습니다.");
        }
        return convertToDto(expert.get());
    }

    // 모든 전문가 조회
    public List<ExpertDto> getAllExperts() {
        return expertRepository.findAll().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    // 활성 전문가만 조회 (공개용)
    public List<ExpertDto> getActiveExperts() {
        return expertRepository.findByStatusOrderByNameAsc("ACTIVE").stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    // 검색 기능
    public List<ExpertDto> searchExperts(String name, String email, String organization, String field, String status) {
        List<Expert> experts = expertRepository.findBySearchCriteria(name, email, organization, field, status);
        return experts.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    // 이름으로 검색
    public List<ExpertDto> searchByName(String name) {
        return expertRepository.findByNameContainingIgnoreCase(name).stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    // 조직으로 검색
    public List<ExpertDto> searchByOrganization(String organization) {
        return expertRepository.findByOrganizationContainingIgnoreCase(organization).stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    // 전문분야로 검색
    public List<ExpertDto> searchByField(String field) {
        return expertRepository.findByFieldContainingIgnoreCase(field).stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    // 상태별 필터링
    public List<ExpertDto> getExpertsByStatus(String status) {
        return expertRepository.findByStatus(status).stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    // 로그인 시간 업데이트
    public void updateLastLogin(Long id) {
        Optional<Expert> expert = expertRepository.findById(id);
        if (expert.isPresent()) {
            Expert expertEntity = expert.get();
            expertEntity.setLastLogin(LocalDateTime.now());
            expertRepository.save(expertEntity);
        }
    }
} 