package com.erns.mot.controller;

import com.erns.mot.domain.Attachment;
import com.erns.mot.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/attachments")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @Autowired
    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping
    public ResponseEntity<Attachment> uploadFile(
            @RequestParam("refTable") String refTable,
            @RequestParam("refId") Long refId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "uploadedBy", required = false) String uploadedBy,
            @RequestParam(value = "note", required = false) String note
    ) throws IOException {
        Attachment attachment = attachmentService.saveFile(refTable, refId, file, uploadedBy, note);
        return ResponseEntity.ok(attachment);
    }

    @GetMapping
    public ResponseEntity<List<Attachment>> getFiles(
            @RequestParam("refTable") String refTable,
            @RequestParam("refId") Long refId
    ) {
        List<Attachment> files = attachmentService.getFiles(refTable, refId);
        return ResponseEntity.ok(files);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        attachmentService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFiles(
            @RequestParam("refTable") String refTable,
            @RequestParam("refId") Long refId
    ) {
        attachmentService.deleteFiles(refTable, refId);
        return ResponseEntity.noContent().build();
    }
} 