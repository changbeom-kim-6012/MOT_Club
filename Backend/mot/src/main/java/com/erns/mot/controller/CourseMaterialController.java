package com.erns.mot.controller;

import com.erns.mot.domain.CourseMaterial;
import com.erns.mot.service.CourseMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/course-materials")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:13000"})
public class CourseMaterialController {

    private final CourseMaterialService courseMaterialService;

    @Autowired
    public CourseMaterialController(CourseMaterialService courseMaterialService) {
        this.courseMaterialService = courseMaterialService;
    }

    @GetMapping
    public ResponseEntity<List<CourseMaterial>> getMaterials(
            @RequestParam("courseId") String courseId,
            @RequestParam(value = "sectionId", required = false) String sectionId,
            @RequestParam(value = "topicId", required = false) String topicId,
            @RequestParam(value = "itemId", required = false) String itemId) {
        
        if (sectionId != null && topicId != null && itemId != null) {
            List<CourseMaterial> materials = courseMaterialService.getMaterialsByCourseAndItem(courseId, sectionId, topicId, itemId);
            return ResponseEntity.ok(materials);
        } else {
            List<CourseMaterial> materials = courseMaterialService.getMaterialsByCourseId(courseId);
            return ResponseEntity.ok(materials);
        }
    }

    @PostMapping
    public ResponseEntity<CourseMaterial> createMaterial(
            @RequestParam("courseId") String courseId,
            @RequestParam("sectionId") String sectionId,
            @RequestParam("topicId") String topicId,
            @RequestParam("itemId") String itemId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("uploadedBy") String uploadedBy,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        
        try {
            CourseMaterial material = courseMaterialService.createMaterial(courseId, sectionId, topicId, itemId, title, description, uploadedBy, file);
            return ResponseEntity.ok(material);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        try {
            courseMaterialService.deleteMaterial(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Resource> viewFile(@PathVariable Long id) {
        try {
            CourseMaterial material = courseMaterialService.getMaterialById(id);
            if (material == null || material.getFilePath() == null) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = Paths.get(material.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // 파일 확장자에 따른 Content-Type 설정
                String contentType = determineContentType(material.getFileName());
                
                return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename*=UTF-8''" + java.net.URLEncoder.encode(material.getFileName(), "UTF-8"))
                    .header("X-Content-Type-Options", "nosniff")
                    .header("Cache-Control", "no-cache")
                    .header("X-Frame-Options", "SAMEORIGIN")
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        try {
            CourseMaterial material = courseMaterialService.getMaterialById(id);
            if (material == null || material.getFilePath() == null) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = Paths.get(material.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + material.getFileName() + "\"")
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private String determineContentType(String fileName) {
        if (fileName == null) return "application/octet-stream";
        
        String extension = fileName.toLowerCase();
        if (extension.endsWith(".pdf")) {
            return "application/pdf";
        } else if (extension.endsWith(".jpg") || extension.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (extension.endsWith(".png")) {
            return "image/png";
        } else if (extension.endsWith(".gif")) {
            return "image/gif";
        } else if (extension.endsWith(".txt")) {
            return "text/plain";
        } else if (extension.endsWith(".doc") || extension.endsWith(".docx")) {
            return "application/msword";
        } else if (extension.endsWith(".xls") || extension.endsWith(".xlsx")) {
            return "application/vnd.ms-excel";
        } else if (extension.endsWith(".ppt") || extension.endsWith(".pptx")) {
            return "application/vnd.ms-powerpoint";
        } else {
            return "application/octet-stream";
        }
    }
} 