package com.campusopportunity.controller;

import com.campusopportunity.dto.EligibilityResponse;
import com.campusopportunity.entity.Opportunity;
import com.campusopportunity.service.EligibilityService;
import com.campusopportunity.service.OpportunityService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/opportunities")
@CrossOrigin(origins = "*")
public class OpportunityController {

    private final OpportunityService opportunityService;
    private final EligibilityService eligibilityService;

    public OpportunityController(OpportunityService opportunityService, EligibilityService eligibilityService) {
        this.opportunityService = opportunityService;
        this.eligibilityService = eligibilityService;
    }

    // Example: /api/opportunities?search=intern&type=Internship&location=Hyderabad&minCgpa=7&branch=CSE
    @GetMapping
    public List<Opportunity> getAll(@RequestParam(required = false) String search,
                                    @RequestParam(required = false) String type,
                                    @RequestParam(required = false) String location,
                                    @RequestParam(required = false) Double minCgpa,
                                    @RequestParam(required = false) String branch) {
        return opportunityService.search(search, type, location, minCgpa, branch);
    }

    @GetMapping("/{id}")
    public Opportunity getById(@PathVariable Long id) {
        return opportunityService.getById(id);
    }

    @PostMapping
    public Opportunity create(@RequestBody Opportunity opportunity) {
        return opportunityService.create(opportunity);
    }

    @PutMapping("/{id}")
    public Opportunity update(@PathVariable Long id, @RequestBody Opportunity opportunity) {
        return opportunityService.update(id, opportunity);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id) {
        opportunityService.delete(id);
        Map<String, String> body = new HashMap<>();
        body.put("message", "Opportunity deleted");
        return body;
    }

    @GetMapping("/{opportunityId}/eligibility/{studentId}")
    public EligibilityResponse checkEligibility(@PathVariable Long opportunityId, @PathVariable Long studentId) {
        return eligibilityService.check(opportunityId, studentId);
    }
}
