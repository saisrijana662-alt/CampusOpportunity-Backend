package com.campusopportunity.dto;

import java.util.List;

public class EligibilityResponse {

    private boolean eligible;

    private List<String> reasons;

    public boolean isEligible() {
        return eligible;
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

}
