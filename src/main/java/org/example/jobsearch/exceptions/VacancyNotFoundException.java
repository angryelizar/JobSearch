package org.example.jobsearch.exceptions;

public class VacancyNotFoundException extends Exception {
    public VacancyNotFoundException() {
    }

    public VacancyNotFoundException(String message) {
        super(message);
    }
}
