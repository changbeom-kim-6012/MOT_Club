package com.erns.mot.controller;

import com.erns.mot.domain.Library;
import com.erns.mot.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/library")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:13000"})
public class LibraryController {

    private final LibraryService libraryService;
    private final Path fileStorageLocation;

    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
        this.fileStorageLocation = Paths.get("uploads/library").toAbsolutePath().normalize();
    }

    @PostMapping
    public ResponseEntity<Library> createLibrary(
            @RequestParam("category") String category,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("description") String description,
            @RequestParam("keywords") String keywords,
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "fileTypes", required = false) String[] fileTypes) {
        try {
            Library library = new Library();
            library.setCategory(category);
            library.setTitle(title);
            library.setAuthor(author);
            library.setDescription(description);
            library.setKeywords(keywords);

            Library createdLibrary = libraryService.createLibrary(library, files, fileTypes);
            return new ResponseEntity<>(createdLibrary, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<Library>> getAllLibraryItems() {
        List<Library> items = libraryService.getAllLibraryItems();
        return ResponseEntity.ok(items);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Library> updateLibrary(
        @PathVariable Long id,
        @RequestParam("category") String category,
        @RequestParam("title") String title,
        @RequestParam("author") String author,
        @RequestParam("description") String description,
        @RequestParam("keywords") String keywords,
        @RequestParam(value = "files", required = false) MultipartFile[] files,
        @RequestParam(value = "deletedFileNames", required = false) String deletedFileNames
    ) {
        try {
            Library updated = libraryService.updateLibrary(id, category, title, author, description, keywords, files, deletedFileNames);
            return ResponseEntity.ok(updated);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibrary(@PathVariable Long id) {
        libraryService.deleteLibrary(id);
        return ResponseEntity.noContent().build();
    }

    // 파일 조회 전용 API (다운로드 방지)
    @GetMapping("/view/{fileName:.+}")
    public ResponseEntity<Resource> viewFile(@PathVariable String fileName) {
        try {
            // URL 디코딩 후 파일명 추출 (더 안전한 처리)
            String decodedFileName = java.net.URLDecoder.decode(fileName, "UTF-8");
            String actualFileName = extractFileName(decodedFileName);
            
            // 특수문자 및 한글 처리 개선
            actualFileName = sanitizeFileName(actualFileName);
            
            System.out.println("=== 파일 조회 디버깅 ===");
            System.out.println("원본 fileName: " + fileName);
            System.out.println("디코딩된 fileName: " + decodedFileName);
            System.out.println("추출된 actualFileName: " + actualFileName);
            System.out.println("=========================");
            
            // 파일 경로 검증 및 보안 체크
            Path filePath = fileStorageLocation.resolve(actualFileName).normalize();
            if (!filePath.startsWith(fileStorageLocation)) {
                System.out.println("보안 위반: 파일 경로가 허용된 디렉토리를 벗어남");
                return ResponseEntity.badRequest().build();
            }
            
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                // 파일 타입에 따른 Content-Type 설정
                String contentType = determineContentType(actualFileName);
                
                // Chrome 차단 방지를 위한 추가 헤더 설정
                return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename*=UTF-8''" + java.net.URLEncoder.encode(actualFileName, "UTF-8"))
                    .header("X-Content-Type-Options", "nosniff")
                    .header("Cache-Control", "no-cache")
                    .header("X-Frame-Options", "SAMEORIGIN")
                    .body(resource);
            } else {
                System.out.println("파일이 존재하지 않거나 읽을 수 없음: " + filePath);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.out.println("파일 조회 중 오류: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // 파일 다운로드 API
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            // URL 디코딩 후 파일명 추출
            String decodedFileName = java.net.URLDecoder.decode(fileName, "UTF-8");
            String actualFileName = extractFileName(decodedFileName);
            
            // 파일 경로 검증 및 보안 체크
            Path filePath = fileStorageLocation.resolve(actualFileName).normalize();
            if (!filePath.startsWith(fileStorageLocation)) {
                System.out.println("보안 위반: 파일 경로가 허용된 디렉토리를 벗어남");
                return ResponseEntity.badRequest().build();
            }
            
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + actualFileName + "\"")
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.out.println("파일 다운로드 중 오류: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // 전체 경로에서 파일명만 추출하는 헬퍼 메서드
    private String extractFileName(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return filePath;
        }
        
        // Windows 경로 구분자 처리
        if (filePath.contains("\\")) {
            return filePath.substring(filePath.lastIndexOf("\\") + 1);
        }
        // Unix 경로 구분자 처리
        else if (filePath.contains("/")) {
            return filePath.substring(filePath.lastIndexOf("/") + 1);
        }
        // 이미 파일명만 있는 경우
        else {
            return filePath;
        }
    }

    // 파일 확장자에 따른 Content-Type 결정
    private String determineContentType(String fileName) {
        String lowerFileName = fileName.toLowerCase();
        if (lowerFileName.endsWith(".pdf")) {
            return "application/pdf";
        } else if (lowerFileName.endsWith(".jpg") || lowerFileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lowerFileName.endsWith(".png")) {
            return "image/png";
        } else if (lowerFileName.endsWith(".gif")) {
            return "image/gif";
        } else if (lowerFileName.endsWith(".txt")) {
            return "text/plain";
        } else if (lowerFileName.endsWith(".doc") || lowerFileName.endsWith(".docx")) {
            return "application/msword";
        } else if (lowerFileName.endsWith(".xls") || lowerFileName.endsWith(".xlsx")) {
            return "application/vnd.ms-excel";
        } else {
            return "application/octet-stream";
        }
    }

    // 파일명 정리 및 특수문자 처리
    private String sanitizeFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return fileName;
        }
        
        // URL 인코딩 문제를 일으킬 수 있는 문자들을 안전하게 처리
        // 실제 파일 시스템에서는 원본 파일명을 유지하되, URL에서는 안전하게 처리
        return fileName.trim();
    }
} 