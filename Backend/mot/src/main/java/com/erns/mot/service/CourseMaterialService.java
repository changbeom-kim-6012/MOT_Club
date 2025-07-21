package com.erns.mot.service;

import com.erns.mot.domain.CourseMaterial;
import com.erns.mot.repository.CourseMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseMaterialService {

    private final CourseMaterialRepository courseMaterialRepository;
    private final Path fileStorageLocation;

    @Autowired
    public CourseMaterialService(CourseMaterialRepository courseMaterialRepository) {
        this.courseMaterialRepository = courseMaterialRepository;
        this.fileStorageLocation = Paths.get("uploads/course-materials").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public List<CourseMaterial> getMaterialsByCourseAndItem(String courseId, String sectionId, String topicId, String itemId) {
        return courseMaterialRepository.findByCourseAndItem(courseId, sectionId, topicId, itemId);
    }

    public List<CourseMaterial> getMaterialsByCourseId(String courseId) {
        return courseMaterialRepository.findByCourseId(courseId);
    }

    public CourseMaterial getMaterialById(Long id) {
        Optional<CourseMaterial> material = courseMaterialRepository.findById(id);
        return material.orElse(null);
    }

    public CourseMaterial createMaterial(String courseId, String sectionId, String topicId, String itemId, 
                                       String title, String description, String uploadedBy, MultipartFile file) throws IOException {
        CourseMaterial material = new CourseMaterial();
        material.setCourseId(courseId);
        material.setSectionId(sectionId);
        material.setTopicId(topicId);
        material.setItemId(itemId);
        material.setTitle(title);
        material.setDescription(description);
        material.setUploadedBy(uploadedBy);

        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String storedFileName = UUID.randomUUID().toString() + "_" + fileName;
            Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetLocation);
            
            material.setFileName(fileName);
            material.setFilePath(targetLocation.toString());
            material.setFileSize(file.getSize());
        }

        return courseMaterialRepository.save(material);
    }

    public void deleteMaterial(Long id) {
        CourseMaterial material = courseMaterialRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("자료를 찾을 수 없습니다."));
        
        // 파일이 있으면 삭제
        if (material.getFilePath() != null) {
            try {
                Files.deleteIfExists(Paths.get(material.getFilePath()));
            } catch (IOException e) {
                // 파일 삭제 실패 시 로그만 남기고 계속 진행
                System.err.println("파일 삭제 실패: " + material.getFilePath());
            }
        }
        
        courseMaterialRepository.deleteById(id);
    }
} 