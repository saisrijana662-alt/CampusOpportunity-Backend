package com.campusopportunity.dto;

import com.campusopportunity.entity.Opportunity;

// id is the SavedOpportunity id (used for unsave)
public class SavedDetails {

    private Long id;

    private Long studentId;

    private Opportunity opportunity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Opportunity getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(Opportunity opportunity) {
        this.opportunity = opportunity;
    }

}
