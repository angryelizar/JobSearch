package org.example.jobsearch.exceptions;

public class UserAlreadyRegisteredException extends Exception{
    public UserAlreadyRegisteredException() {
    }

    public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}
