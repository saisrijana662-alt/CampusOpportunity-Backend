package com.campusopportunity.service;

import com.campusopportunity.config.ApiException;
import com.campusopportunity.entity.Opportunity;
import com.campusopportunity.repository.ApplicationRepository;
import com.campusopportunity.repository.OpportunityRepository;
import com.campusopportunity.repository.SavedOpportunityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpportunityService {

    private final OpportunityRepository opportunityRepository;
    private final ApplicationRepository applicationRepository;
    private final SavedOpportunityRepository savedRepository;

    public OpportunityService(OpportunityRepository opportunityRepository,
                              ApplicationRepository applicationRepository,
                              SavedOpportunityRepository savedRepository) {
        this.opportunityRepository = opportunityRepository;
        this.applicationRepository = applicationRepository;
        this.savedRepository = savedRepository;
    }

    // All filters are optional. Filtering is done in plain Java so it is easy to read.
    public List<Opportunity> search(String search, String type, String location, Double minCgpa, String branch) {
        List<Opportunity> result = new ArrayList<>();

        for (Opportunity o : opportunityRepository.findAll()) {
            if (!isBlank(search)) {
                String text = search.trim().toLowerCase();
                boolean inTitle = o.getTitle() != null && o.getTitle().toLowerCase().contains(text);
                boolean inCompany = o.getCompany() != null && o.getCompany().toLowerCase().contains(text);
                if (!inTitle && !inCompany) continue;
            }
            if (!isBlank(type) && !type.equalsIgnoreCase(o.getType())) continue;

            if (!isBlank(location)) {
                if (o.getLocation() == null || !o.getLocation().toLowerCase().contains(location.trim().toLowerCase())) continue;
            }

            // "minimum CGPA" filter = show opportunities whose required CGPA is at most this value
            if (minCgpa != null) {
                double required = o.getMinCgpa() == null ? 0 : o.getMinCgpa();
                if (required > minCgpa) continue;
            }

            if (!isBlank(branch) && !EligibilityService.branchAllowed(o.getEligibleBranch(), branch)) continue;

            result.add(o);
        }
        return result;
    }

    public Opportunity getById(Long id) {
        return opportunityRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Opportunity not found"));
    }

    public Opportunity create(Opportunity opportunity) {
        validate(opportunity);
        opportunity.setId(null);
        return opportunityRepository.save(opportunity);
    }

    public Opportunity update(Long id, Opportunity updated) {
        Opportunity o = getById(id);
        validate(updated);
        o.setTitle(updated.getTitle());
        o.setCompany(updated.getCompany());
        o.setType(updated.getType());
        o.setDescription(updated.getDescription());
        o.setLocation(updated.getLocation());
        o.setDeadline(updated.getDeadline());
        o.setMinCgpa(updated.getMinCgpa());
        o.setEligibleBranch(updated.getEligibleBranch());
        o.setGraduationYear(updated.getGraduationYear());
        o.setRequiredSkills(updated.getRequiredSkills());
        o.setApplicationLink(updated.getApplicationLink());
        o.setMaxBacklogs(updated.getMaxBacklogs());
        return opportunityRepository.save(o);
    }

    // Also removes applications and saved entries of this opportunity.
    @Transactional
    public void delete(Long id) {
        getById(id);
        applicationRepository.deleteByOpportunityId(id);
        savedRepository.deleteByOpportunityId(id);
        opportunityRepository.deleteById(id);
    }

    private void validate(Opportunity o) {
        if (isBlank(o.getTitle()) || isBlank(o.getCompany()) || isBlank(o.getType())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Title, company and type are required");
        }
        List<String> types = List.of("Internship", "Hackathon", "Placement", "Scholarship", "Competition");
        if (!types.contains(o.getType())) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Invalid type. Use: Internship, Hackathon, Placement, Scholarship or Competition");
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
