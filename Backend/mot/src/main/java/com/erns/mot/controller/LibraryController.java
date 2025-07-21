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
import java.util.List;

@RestController
@RequestMapping("/api/library")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:13000"})
public class LibraryController {

    private final LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping
    public ResponseEntity<Library> createLibrary(
            @RequestParam("category") String category,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("description") String description,
            @RequestParam("keywords") String keywords,
            @RequestParam("files") MultipartFile[] files) {
        try {
            Library library = new Library();
            library.setCategory(category);
            library.setTitle(title);
            library.setAuthor(author);
            library.setDescription(description);
            library.setKeywords(keywords);

            Library createdLibrary = libraryService.createLibrary(library, files);
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
} 