package org.example.jobsearch.exceptions;

public class UserHaveTooLowAgeException extends Exception{
    public UserHaveTooLowAgeException() {
    }

    public UserHaveTooLowAgeException(String message) {
        super(message);
    }
}
