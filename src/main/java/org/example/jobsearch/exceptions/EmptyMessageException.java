package org.example.jobsearch.exceptions;

public class EmptyMessageException extends IllegalArgumentException{
    public EmptyMessageException() {
    }

    public EmptyMessageException(String s) {
        super(s);
    }
}
