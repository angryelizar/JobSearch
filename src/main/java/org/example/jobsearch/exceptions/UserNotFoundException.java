package org.example.jobsearch.exceptions;

import lombok.experimental.StandardException;

@StandardException
public class UserNotFoundException extends Exception{
    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
