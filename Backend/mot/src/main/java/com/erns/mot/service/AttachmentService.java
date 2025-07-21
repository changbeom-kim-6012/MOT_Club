package com.erns.mot.service;

import com.erns.mot.domain.Attachment;
import com.erns.mot.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final Path fileStorageLocation;

    @Autowired
    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
        this.fileStorageLocation = Paths.get("uploads/attachments").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Transactional
    public Attachment saveFile(String refTable, Long refId, MultipartFile file, String uploadedBy, String note) throws IOException {
        String fileName = file.getOriginalFilename();
        String storedFileName = UUID.randomUUID().toString() + "_" + fileName;
        Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
        Files.copy(file.getInputStream(), targetLocation);

        Attachment attachment = new Attachment();
        attachment.setRefTable(refTable);
        attachment.setRefId(refId);
        attachment.setFileName(fileName);
        attachment.setFilePath(targetLocation.toString());
        attachment.setFileSize(file.getSize());
        attachment.setUploadedBy(uploadedBy);
        attachment.setNote(note);
        return attachmentRepository.save(attachment);
    }

    public List<Attachment> getFiles(String refTable, Long refId) {
        return attachmentRepository.findByRefTableAndRefId(refTable, refId);
    }

    @Transactional
    public void deleteFiles(String refTable, Long refId) {
        attachmentRepository.deleteByRefTableAndRefId(refTable, refId);
    }

    @Transactional
    public void deleteFile(Long id) {
        attachmentRepository.deleteById(id);
    }
} 