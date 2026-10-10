package com.campusopportunity.controller;

import com.campusopportunity.dto.ApplicationDetails;
import com.campusopportunity.dto.ApplyRequest;
import com.campusopportunity.dto.StatusRequest;
import com.campusopportunity.entity.Application;
import com.campusopportunity.service.ApplicationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public Application apply(@RequestBody ApplyRequest request) {
        return applicationService.apply(request.getStudentId(), request.getOpportunityId());
    }

    @GetMapping("/student/{studentId}")
    public List<ApplicationDetails> getByStudent(@PathVariable Long studentId) {
        return applicationService.getByStudent(studentId);
    }

    @PutMapping("/{id}/status")
    public Application updateStatus(@PathVariable Long id, @RequestBody StatusRequest request) {
        return applicationService.updateStatus(id, request.getStatus());
    }
}
