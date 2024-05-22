package org.example.jobsearch.enums;

public enum ResponseStatus {
    IN_PROGRESS("applicant.in_progress"),
    APPROVED("applicant.approved"),
    REJECTED("applicant.rejected");

    private String value;

    private ResponseStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
