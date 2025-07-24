package com.erns.mot.controller;

import com.erns.mot.domain.Opinion;
import com.erns.mot.service.OpinionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/api/opinions")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:13000"})
public class OpinionController {

    private final OpinionService opinionService;

    @Autowired
    public OpinionController(OpinionService opinionService) {
        this.opinionService = opinionService;
    }

    @PostMapping
    public ResponseEntity<Opinion> createOpinion(@RequestBody Opinion opinion) {
        return ResponseEntity.ok(opinionService.createOpinion(opinion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Opinion> updateOpinion(@PathVariable Long id, @RequestBody Opinion opinion) {
        return ResponseEntity.ok(opinionService.updateOpinion(id, opinion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOpinion(@PathVariable Long id) {
        opinionService.deleteOpinion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Opinion> getOpinion(@PathVariable Long id) {
        Optional<Opinion> opinion = opinionService.getOpinion(id);
        return opinion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Opinion>> getAllOpinions() {
        System.out.println("=== getAllOpinions called ===");
        System.out.println("Request received for /api/opinions");
        try {
            List<Opinion> opinions = opinionService.getAllOpinions();
            System.out.println("Found " + opinions.size() + " opinions");
            return ResponseEntity.ok(opinions);
        } catch (Exception e) {
            System.out.println("Error in getAllOpinions: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Opinion> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            System.out.println("=== Status Update Request ===");
            System.out.println("ID: " + id);
            System.out.println("Request Body: " + request);
            
            String status = request.get("status");
            System.out.println("Status: " + status);
            
            if (status == null || status.trim().isEmpty()) {
                System.out.println("Status is null or empty");
                return ResponseEntity.badRequest().build();
            }
            
            Opinion updatedOpinion = opinionService.updateStatus(id, status);
            System.out.println("Updated Opinion: " + updatedOpinion.getTitle() + " - " + updatedOpinion.getStatus());
            return ResponseEntity.ok(updatedOpinion);
        } catch (Exception e) {
            System.out.println("Error in updateStatus: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // 간단한 테스트용 API
    @PutMapping("/{id}/status-simple")
    public ResponseEntity<Opinion> updateStatusSimple(@PathVariable Long id, @RequestParam String status) {
        try {
            System.out.println("=== Simple Status Update ===");
            System.out.println("ID: " + id + ", Status: " + status);
            
            Opinion updatedOpinion = opinionService.updateStatus(id, status);
            return ResponseEntity.ok(updatedOpinion);
        } catch (Exception e) {
            System.out.println("Error in simple update: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // 헬스체크 API
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        try {
            List<Opinion> opinions = opinionService.getAllOpinions();
            Map<String, String> response = Map.of(
                "status", "OK",
                "message", "Opinion service is running",
                "count", String.valueOf(opinions.size())
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = Map.of(
                "status", "ERROR",
                "message", "Opinion service error: " + e.getMessage()
            );
            return ResponseEntity.status(500).body(response);
        }
    }
} 