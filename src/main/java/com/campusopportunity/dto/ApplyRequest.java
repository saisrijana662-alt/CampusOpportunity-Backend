package com.campusopportunity.dto;


// Used for both /api/applications and /api/saved
public class ApplyRequest {

    private Long studentId;

    private Long opportunityId;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(Long opportunityId) {
        this.opportunityId = opportunityId;
    }

}
