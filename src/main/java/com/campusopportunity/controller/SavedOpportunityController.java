package com.campusopportunity.controller;

import com.campusopportunity.dto.ApplyRequest;
import com.campusopportunity.dto.SavedDetails;
import com.campusopportunity.entity.SavedOpportunity;
import com.campusopportunity.service.SavedOpportunityService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/saved")
@CrossOrigin(origins = "*")
public class SavedOpportunityController {

    private final SavedOpportunityService savedService;

    public SavedOpportunityController(SavedOpportunityService savedService) {
        this.savedService = savedService;
    }

    @PostMapping
    public SavedOpportunity save(@RequestBody ApplyRequest request) {
        return savedService.save(request.getStudentId(), request.getOpportunityId());
    }

    @DeleteMapping("/{id}")
    public Map<String, String> remove(@PathVariable Long id) {
        savedService.remove(id);
        Map<String, String> body = new HashMap<>();
        body.put("message", "Removed from saved");
        return body;
    }

    @GetMapping("/student/{studentId}")
    public List<SavedDetails> getByStudent(@PathVariable Long studentId) {
        return savedService.getByStudent(studentId);
    }
}
