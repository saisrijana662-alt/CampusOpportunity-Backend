package com.campusopportunity.service;

import com.campusopportunity.config.ApiException;
import com.campusopportunity.dto.SavedDetails;
import com.campusopportunity.entity.SavedOpportunity;
import com.campusopportunity.repository.SavedOpportunityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SavedOpportunityService {

    private final SavedOpportunityRepository savedRepository;
    private final StudentService studentService;
    private final OpportunityService opportunityService;

    public SavedOpportunityService(SavedOpportunityRepository savedRepository,
                                   StudentService studentService,
                                   OpportunityService opportunityService) {
        this.savedRepository = savedRepository;
        this.studentService = studentService;
        this.opportunityService = opportunityService;
    }

    public SavedOpportunity save(Long studentId, Long opportunityId) {
        if (studentId == null || opportunityId == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "studentId and opportunityId are required");
        }
        studentService.getById(studentId);
        opportunityService.getById(opportunityId);

        if (savedRepository.existsByStudentIdAndOpportunityId(studentId, opportunityId)) {
            throw new ApiException(HttpStatus.CONFLICT, "Opportunity is already saved");
        }
        SavedOpportunity saved = new SavedOpportunity();
        saved.setStudentId(studentId);
        saved.setOpportunityId(opportunityId);
        return savedRepository.save(saved);
    }

    public void remove(Long id) {
        if (!savedRepository.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Saved opportunity not found");
        }
        savedRepository.deleteById(id);
    }

    public List<SavedDetails> getByStudent(Long studentId) {
        studentService.getById(studentId);
        List<SavedDetails> list = new ArrayList<>();
        for (SavedOpportunity s : savedRepository.findByStudentId(studentId)) {
            SavedDetails d = new SavedDetails();
            d.setId(s.getId());
            d.setStudentId(s.getStudentId());
            d.setOpportunity(opportunityService.getById(s.getOpportunityId()));
            list.add(d);
        }
        return list;
    }
}
