package org.example.jobsearch.exceptions;

public class UserHaveTooLowAge extends Exception{
    public UserHaveTooLowAge() {
    }

    public UserHaveTooLowAge(String message) {
        super(message);
    }
}
