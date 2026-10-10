package com.campusopportunity.service;

import com.campusopportunity.dto.EligibilityResponse;
import com.campusopportunity.entity.Opportunity;
import com.campusopportunity.entity.Student;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Compares a student's profile with an opportunity and explains EXACTLY why not eligible.
@Service
public class EligibilityService {

    private final StudentService studentService;
    private final OpportunityService opportunityService;

    public EligibilityService(StudentService studentService, OpportunityService opportunityService) {
        this.studentService = studentService;
        this.opportunityService = opportunityService;
    }

    public EligibilityResponse check(Long opportunityId, Long studentId) {
        Student student = studentService.getById(studentId);
        Opportunity opp = opportunityService.getById(opportunityId);

        List<String> reasons = new ArrayList<>();

        // 1. CGPA
        if (opp.getMinCgpa() != null && opp.getMinCgpa() > 0) {
            if (student.getCgpa() == null || student.getCgpa() < opp.getMinCgpa()) {
                reasons.add("CGPA is below required CGPA (required " + opp.getMinCgpa()
                        + ", yours " + (student.getCgpa() == null ? "not set" : student.getCgpa()) + ")");
            }
        }

        // 2. Branch
        if (!branchAllowed(opp.getEligibleBranch(), student.getBranch())) {
            reasons.add("Branch is not eligible (allowed: " + opp.getEligibleBranch()
                    + ", yours: " + (student.getBranch() == null ? "not set" : student.getBranch()) + ")");
        }

        // 3. Graduation year
        if (opp.getGraduationYear() != null && opp.getGraduationYear() > 0) {
            if (student.getGraduationYear() == null || !student.getGraduationYear().equals(opp.getGraduationYear())) {
                reasons.add("Graduation year does not match (required " + opp.getGraduationYear()
                        + ", yours " + (student.getGraduationYear() == null ? "not set" : student.getGraduationYear()) + ")");
            }
        }

        // 4. Required skills
        if (opp.getRequiredSkills() != null && !opp.getRequiredSkills().trim().isEmpty()) {
            List<String> studentSkills = splitList(student.getSkills());
            for (String required : opp.getRequiredSkills().split(",")) {
                required = required.trim();
                if (!required.isEmpty() && !studentSkills.contains(required.toLowerCase())) {
                    reasons.add("Missing required skill: " + required);
                }
            }
        }

        // 5. Backlogs (only if the opportunity has a backlog condition)
        if (opp.getMaxBacklogs() != null) {
            int backlogs = student.getBacklogs() == null ? 0 : student.getBacklogs();
            if (backlogs > opp.getMaxBacklogs()) {
                reasons.add("Too many backlogs (allowed " + opp.getMaxBacklogs() + ", yours " + backlogs + ")");
            }
        }

        // 6. Deadline
        if (opp.getDeadline() != null && opp.getDeadline().isBefore(LocalDate.now())) {
            reasons.add("Deadline has passed (" + opp.getDeadline() + ")");
        }

        EligibilityResponse response = new EligibilityResponse();
        response.setEligible(reasons.isEmpty());
        response.setReasons(reasons);
        return response;
    }

    // true if eligibleBranch is empty / "All" / contains the given branch (comma separated list)
    public static boolean branchAllowed(String eligibleBranch, String branch) {
        if (eligibleBranch == null || eligibleBranch.trim().isEmpty()) return true;
        List<String> allowed = splitList(eligibleBranch);
        if (allowed.contains("all")) return true;
        return branch != null && allowed.contains(branch.trim().toLowerCase());
    }

    // "Java, SQL" -> ["java", "sql"]  (lower case, trimmed)
    private static List<String> splitList(String text) {
        List<String> list = new ArrayList<>();
        if (text == null) return list;
        for (String part : text.split(",")) {
            String p = part.trim().toLowerCase();
            if (!p.isEmpty()) list.add(p);
        }
        return list;
    }
}
