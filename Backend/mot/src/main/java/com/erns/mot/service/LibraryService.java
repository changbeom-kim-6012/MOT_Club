package com.erns.mot.service;

import com.erns.mot.domain.Library;
import com.erns.mot.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final Path fileStorageLocation;

    @Autowired
    public LibraryService(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
        this.fileStorageLocation = Paths.get("uploads/library").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public Library createLibrary(Library library, MultipartFile[] files) throws IOException {
        StringBuilder fileNames = new StringBuilder();
        StringBuilder filePaths = new StringBuilder();
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String fileName = file.getOriginalFilename();
            String storedFileName = UUID.randomUUID().toString() + "_" + fileName;
            Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetLocation);
            fileNames.append(fileName);
            filePaths.append(targetLocation.toString());
            if (i < files.length - 1) {
                fileNames.append(",");
                filePaths.append(",");
            }
        }
        library.setFileNames(fileNames.toString());
        library.setFilePaths(filePaths.toString());
        return libraryRepository.save(library);
    }

    public List<Library> getAllLibraryItems() {
        return libraryRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Library updateLibrary(Long id, String category, String title, String author, String description, String keywords, MultipartFile[] files, String deletedFileNames) throws IOException {
        Library library = libraryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("자료를 찾을 수 없습니다."));
        library.setCategory(category);
        library.setTitle(title);
        library.setAuthor(author);
        library.setDescription(description);
        library.setKeywords(keywords);
        
        // 삭제할 파일 처리
        String existingFileNames = library.getFileNames() != null ? library.getFileNames() : "";
        String existingFilePaths = library.getFilePaths() != null ? library.getFilePaths() : "";
        
        if (deletedFileNames != null && !deletedFileNames.isEmpty()) {
            String[] existingNames = existingFileNames.split(",");
            String[] existingPaths = existingFilePaths.split(",");
            String[] toDelete = deletedFileNames.split(",");
            
            StringBuilder newFileNames = new StringBuilder();
            StringBuilder newFilePaths = new StringBuilder();
            
            for (int i = 0; i < existingNames.length; i++) {
                boolean shouldDelete = false;
                for (String deleteName : toDelete) {
                    if (existingNames[i].trim().equals(deleteName.trim())) {
                        shouldDelete = true;
                        break;
                    }
                }
                
                if (!shouldDelete) {
                    if (newFileNames.length() > 0) {
                        newFileNames.append(",");
                        newFilePaths.append(",");
                    }
                    newFileNames.append(existingNames[i]);
                    newFilePaths.append(existingPaths[i]);
                }
            }
            
            existingFileNames = newFileNames.toString();
            existingFilePaths = newFilePaths.toString();
        }
        
        if (files != null && files.length > 0) {
            StringBuilder fileNames = new StringBuilder();
            StringBuilder filePaths = new StringBuilder();
            
            // 기존 파일 정보가 있으면 먼저 추가
            if (!existingFileNames.isEmpty()) {
                fileNames.append(existingFileNames);
                filePaths.append(existingFilePaths);
            }
            
            // 새 파일들 추가
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                if (file != null && !file.isEmpty()) {
                    String fileName = file.getOriginalFilename();
                    String storedFileName = UUID.randomUUID().toString() + "_" + fileName;
                    Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
                    Files.copy(file.getInputStream(), targetLocation);
                    
                    // 기존 파일이 있으면 쉼표 추가
                    if (fileNames.length() > 0) {
                        fileNames.append(",");
                        filePaths.append(",");
                    }
                    
                    fileNames.append(fileName);
                    filePaths.append(targetLocation.toString());
                }
            }
            
            library.setFileNames(fileNames.toString());
            library.setFilePaths(filePaths.toString());
        } else {
            // 새 파일이 없으면 삭제된 파일만 반영
            library.setFileNames(existingFileNames);
            library.setFilePaths(existingFilePaths);
        }
        return libraryRepository.save(library);
    }

    public void deleteLibrary(Long id) {
        libraryRepository.deleteById(id);
    }
} 