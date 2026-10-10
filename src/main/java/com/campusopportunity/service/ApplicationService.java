package com.campusopportunity.service;

import com.campusopportunity.config.ApiException;
import com.campusopportunity.dto.ApplicationDetails;
import com.campusopportunity.entity.Application;
import com.campusopportunity.entity.Opportunity;
import com.campusopportunity.repository.ApplicationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationService {

    public static final List<String> STATUSES =
            List.of("Applied", "Assessment", "Interview", "Selected", "Rejected");

    private final ApplicationRepository applicationRepository;
    private final StudentService studentService;
    private final OpportunityService opportunityService;

    public ApplicationService(ApplicationRepository applicationRepository,
                              StudentService studentService,
                              OpportunityService opportunityService) {
        this.applicationRepository = applicationRepository;
        this.studentService = studentService;
        this.opportunityService = opportunityService;
    }

    public Application apply(Long studentId, Long opportunityId) {
        if (studentId == null || opportunityId == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "studentId and opportunityId are required");
        }
        studentService.getById(studentId);                       // throws if student not found
        Opportunity opp = opportunityService.getById(opportunityId); // throws if opportunity not found

        if (applicationRepository.existsByStudentIdAndOpportunityId(studentId, opportunityId)) {
            throw new ApiException(HttpStatus.CONFLICT, "You have already applied to this opportunity");
        }
        if (opp.getDeadline() != null && opp.getDeadline().isBefore(LocalDate.now())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "The deadline for this opportunity has passed");
        }

        Application application = new Application();
        application.setStudentId(studentId);
        application.setOpportunityId(opportunityId);
        application.setAppliedDate(LocalDate.now());
        application.setStatus("Applied");
        return applicationRepository.save(application);
    }

    public List<ApplicationDetails> getByStudent(Long studentId) {
        studentService.getById(studentId);
        List<ApplicationDetails> list = new ArrayList<>();
        for (Application a : applicationRepository.findByStudentId(studentId)) {
            Opportunity opp = opportunityService.getById(a.getOpportunityId());
            ApplicationDetails d = new ApplicationDetails();
            d.setId(a.getId());
            d.setStudentId(a.getStudentId());
            d.setOpportunityId(a.getOpportunityId());
            d.setOpportunityTitle(opp.getTitle());
            d.setCompany(opp.getCompany());
            d.setType(opp.getType());
            d.setDeadline(opp.getDeadline());
            d.setAppliedDate(a.getAppliedDate());
            d.setStatus(a.getStatus());
            list.add(d);
        }
        return list;
    }

    public Application updateStatus(Long id, String status) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Application not found"));
        if (status == null || !STATUSES.contains(status)) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Invalid status. Use: Applied, Assessment, Interview, Selected or Rejected");
        }
        application.setStatus(status);
        return applicationRepository.save(application);
    }
}
